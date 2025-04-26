package it.andrea.start.service.initialize;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.quartz.SimpleTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import it.andrea.start.constants.EntityType;
import it.andrea.start.constants.RoleType;
import it.andrea.start.constants.UserStatus;
import it.andrea.start.error.exception.user.UserRoleNotFoundException;
import it.andrea.start.models.JobInfo;
import it.andrea.start.models.user.User;
import it.andrea.start.models.user.UserRole;
import it.andrea.start.repository.JobInfoRepository;
import it.andrea.start.repository.user.UserRepository;
import it.andrea.start.repository.user.UserRoleRepository;
import it.andrea.start.security.EncrypterManager;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class InitializeServiceImpl {

    private static final Logger LOG = LoggerFactory.getLogger(InitializeServiceImpl.class);
    private static final String XML_FILE = ".xml";
    private static final String JOBS_FILE = "jobs" + XML_FILE;
    private static final String USERS_FILE = "users" + XML_FILE;

    private final EncrypterManager encrypterManager;
    private final DocumentBuilderFactory documentBuilderFactory;

    private final JobInfoRepository jobInfoRepository;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    @Value("${app.initialize.file.path}")
    private String appPath;

    @PostConstruct
    public void executeStartOperation() {
        initRoles();
        loadJobsUsersFromXML(appPath + USERS_FILE);
        loadJobsFromXML(appPath + JOBS_FILE);
    }

    public void initRoles() {
        Arrays.stream(RoleType.values()).forEach(roleType -> {
            if (!userRoleRepository.existsByRole(roleType)) {
                userRoleRepository.save(new UserRole(roleType));
            }
        });
    }

    private void loadJobsUsersFromXML(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.canRead()) {
                LOG.warn("File utenti non trovato o non leggibile: {}", filePath);
                return;
            }
            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("user");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    try {
                        User user = createUserFromElement(element);
                        if (userRepository.findByUsername(user.getUsername()).isEmpty()) {
                            user.setCreator(EntityType.SYSTEM.getValue());
                            user.setLastModifiedBy(EntityType.SYSTEM.getValue());
                            userRepository.save(user);
                            LOG.info("Utente '{}' creato da XML.", user.getUsername());
                        } else {
                            LOG.warn("Utente '{}' già presente nel database, saltato da XML.", user.getUsername());
                        }
                    } catch (IllegalArgumentException | NullPointerException e) {
                        LOG.error("Errore durante la creazione di User dall'elemento XML #{}: {}", i + 1, e.getMessage(), e);
                    }
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            logXmlError(e, filePath);
        } catch (Exception e) {
            logUnexpectedError(e, "caricamento utenti da XML " + filePath);
        }
    }

    private User createUserFromElement(Element element) {
        User user = new User();
        user.setUsername(getRequiredTagValue("username", element));
        user.setPassword(encrypterManager.encode(getRequiredTagValue("password", element)));
        user.setEmail(getRequiredTagValue("email", element));

        user.setName(getTagValueOrNull("name", element));
        user.setUserStatus(UserStatus.valueOf(getRequiredTagValue("userStatus", element)));
        user.setLanguageDefault(getTagValueOrNull("languageDefault", element));

        Set<UserRole> userRoles = new HashSet<>();
        NodeList rolesList = element.getElementsByTagName("role");
        for (int i = 0; i < rolesList.getLength(); i++) {
            Node roleNode = rolesList.item(i);
            if (roleNode.getNodeType() == Node.ELEMENT_NODE) {
                Element roleElement = (Element) roleNode;
                String roleName = roleElement.getTextContent();
                if (roleName != null && !roleName.trim().isEmpty()) {
                    try {
                        RoleType roleType = RoleType.valueOf(roleName.trim().toUpperCase());
                        Optional<UserRole> userRoleOpt = userRoleRepository.findByRole(roleType);
                        userRoleOpt.ifPresentOrElse(
                                userRoles::add,
                                () -> new UserRoleNotFoundException(roleName));
                    } catch (IllegalArgumentException e) {
                        LOG.warn("Valore ruolo non valido '{}' per l'utente '{}'. Ignorato.", roleName.trim(), user.getUsername());
                    }
                }
            }
        }
        user.setRoles(userRoles);

        return user;
    }

    public void loadJobsFromXML(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.canRead()) {
                LOG.warn("File job non trovato o non leggibile: {}", filePath);
                return;
            }

            DocumentBuilder builder = documentBuilderFactory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList nodeList = doc.getElementsByTagName("job");

            int jobsSaved = 0;
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    try {
                        JobInfo jobInfo = createJobInfoFromElement(element);
                        jobInfoRepository.save(jobInfo);
                        jobsSaved++;
                    } catch (IllegalArgumentException | NullPointerException e) {
                        LOG.error("Errore durante la creazione di JobInfo dall'elemento XML #{}: {}", i + 1, e.getMessage(), e);
                    }
                }
            }

            LOG.info("Caricamento job da XML completato. Salvati/Aggiornati {} job.", jobsSaved);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            logXmlError(e, filePath);
        } catch (Exception e) {
            logUnexpectedError(e, "caricamento job da XML " + filePath);
        }
    }

    private JobInfo createJobInfoFromElement(Element element) {
        JobInfo jobInfo = new JobInfo();

        jobInfo.setJobName(getRequiredTagValue("name", element));
        jobInfo.setJobGroup(getRequiredTagValue("group", element));
        jobInfo.setJobClass(getRequiredTagValue("class", element));

        jobInfo.setDescription(getTagValueOrNull("description", element));
        jobInfo.setCronExpression(getTagValueOrNull("cronExpression", element));
        jobInfo.setJobDataMapJson(getTagValueOrNull("jobDataMapJson", element));

        jobInfo.setRepeatIntervalMillis(parseLongOrNull(getTagValueOrNull("repeatIntervalMillis", element)));
        jobInfo.setInitialDelayMillis(parseLongOrNull(getTagValueOrNull("initialDelayMillis", element)));
        jobInfo.setRepeatCount(parseIntOrNull(getTagValueOrNull("repeatCount", element)));

        jobInfo.setCronJob(parseBooleanOrDefault(getTagValueOrNull("cronJob", element), false));
        jobInfo.setActive(parseBooleanOrDefault(getTagValueOrNull("isActive", element), false));

        if (jobInfo.isCronJob()) {
            if (jobInfo.getCronExpression() == null || jobInfo.getCronExpression().isBlank()) {
                throw new IllegalArgumentException("Il campo <cronExpression> è obbligatorio quando <cronJob> è true per il job: " + jobInfo.getJobName());
            }
            jobInfo.setRepeatIntervalMillis(null);
            jobInfo.setInitialDelayMillis(null);
            jobInfo.setRepeatCount(null);
        } else {
            if (jobInfo.getRepeatIntervalMillis() == null || jobInfo.getRepeatIntervalMillis() <= 0) {
                throw new IllegalArgumentException("Il campo <repeatIntervalMillis> (con valore > 0) è obbligatorio quando <cronJob> è false per il job: " + jobInfo.getJobName());
            }

            jobInfo.setCronExpression(null);
        }

        return jobInfo;
    }

    private String getRequiredTagValue(String tagName, Element parentElement) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() == 0 || nodeList.item(0).getTextContent() == null || nodeList.item(0).getTextContent().trim().isEmpty()) {
            String parentIdentifier = parentElement.getElementsByTagName("name").item(0).getTextContent();
            throw new IllegalArgumentException("Tag obbligatorio mancante o vuoto: <" + tagName + "> nell'elemento: " + parentIdentifier);
        }
        return nodeList.item(0).getTextContent().trim();
    }

    private String getTagValueOrNull(String tagName, Element parentElement) {
        NodeList nodeList = parentElement.getElementsByTagName(tagName);
        if (nodeList.getLength() > 0 && nodeList.item(0).getTextContent() != null) {
            String value = nodeList.item(0).getTextContent().trim();
            return value.isEmpty() ? null : value;
        }
        return null;
    }

    private Long parseLongOrNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Long.parseLong(value.trim());
        } catch (NumberFormatException e) {
            LOG.warn("Valore non valido per Long: '{}'. Verrà trattato come null.", value);
            return null;
        }
    }

    private Integer parseIntOrNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String trimmedValue = value.trim();
        try {
            if ("-1".equals(trimmedValue) || "REPEAT_INDEFINITELY".equalsIgnoreCase(trimmedValue)) {
                return SimpleTrigger.REPEAT_INDEFINITELY;
            }
            return Integer.parseInt(trimmedValue);
        } catch (NumberFormatException e) {
            LOG.warn("Valore non valido per Integer: '{}'. Verrà trattato come null.", trimmedValue);
            return null;
        }
    }

    private Boolean parseBooleanOrDefault(String value, boolean defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        return "true".equalsIgnoreCase(value.trim());
    }

    private void logXmlError(Exception e, String filePath) {
        LOG.error("Errore durante il processing del documento XML '{}': {}", filePath, e.getMessage(), e);
    }

    private void logUnexpectedError(Exception e, String operationDescription) {
        LOG.error("Eccezione non gestita durante {}: {}", operationDescription, e.getMessage(), e);
    }

}