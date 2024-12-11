package com.epam.esm.service.impl;

import com.epam.esm.repository.CertificateRepository;
import com.epam.esm.repository.OrderRepository;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.UserRepository;
import com.epam.esm.repository.model.*;
import com.epam.esm.service.RandomDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class RandomDataServiceImpl implements RandomDataService {

    private static final int COUNT_USER = 100;
    private static final int COUNT_TAG = 50;
    private static final int CERTIFICATE_MAX_TAGS = 10;
    private static final int COUNT_CERTIFICATE = 100;
    private static final int COUNT_ORDER = 1000;
    private static final int MAX_SOLD_CERTIFICATE_FOR_ORDER = 10;
    public static final String DICTIONARIES_LOGINS = "logins.txt";
    public static final String DICTIONARIES_FIRSTNAMES = "firstnames.txt";
    public static final String DICTIONARIES_SURNAME = "surname.txt";
    public static final String DICTIONARIES_WORDS = "words.txt";
    private static final int CERTIFICATE_DESCRIPTION_MAX_LENGTH = 10;
    private static final int CERTIFICATE_DESCRIPTION_MIN_LENGTH = 2;
    private static final int CERTIFICATE_MIN_DURATION = 10;
    private static final int CERTIFICATE_MAX_DURATION = 365;
    private static final int CERTIFICATE_MIN_PRICE = 5;
    private static final int CERTIFICATE_MAX_PRICE = 100000;
    private static final int CERTIFICATE_MIN_TAGS = 1;
    public static final String MIN_RANDOM_DATE = "2021-01-01 00:00:00";
    public static final String MAX_RANDOM_DATE = "2022-01-01 00:00:00";
    private static final int MAX_COUNT_CERTIFICATE_FOR_SOLD = 3;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final CertificateRepository certificateRepository;
    private final OrderRepository orderRepository;
    private final Random random = new Random();
    private final Set<String> usedTagName = new HashSet<>();
    private final Set<String> usedLogins = new HashSet<>();
    private final List<Long> userIds = new ArrayList<>();
    private final List<Long> tagIds = new ArrayList<>();
    private final List<Long> certificateIds = new ArrayList<>();
    private List<String> logins;
    private List<String> firstnames;
    private List<String> surnames;
    private List<String> words;

    @Override
    @Transactional
    public void generateDataInDb(String dictionariesPath) {
        long timeStart = System.currentTimeMillis();
        loadAllDictionaries(dictionariesPath);
        fillUsers(COUNT_USER);
        log.info("generated users: " + COUNT_USER);
        fillTags(COUNT_TAG);
        log.info("generated tags: " + COUNT_TAG);
        fillCertificates(COUNT_CERTIFICATE);
        log.info("generated certificate: " + COUNT_CERTIFICATE);
        fillOrders(COUNT_ORDER);
        log.info("generated order: " + COUNT_ORDER);
        log.info("all data generated in " + (double) (System.currentTimeMillis() - timeStart) / 1000 + " seconds");
    }

    private void loadAllDictionaries(String dictionariesPath) {
        try {
            logins = loadDictionary(ResourceUtils.getFile(dictionariesPath + "\\" + DICTIONARIES_LOGINS));
            log.info("load: " + logins.size() + " logins");
            firstnames = loadDictionary(ResourceUtils.getFile(dictionariesPath + "\\" + DICTIONARIES_FIRSTNAMES));
            log.info("load: " + firstnames.size() + " firstnames");
            surnames = loadDictionary(ResourceUtils.getFile(dictionariesPath + "\\" + DICTIONARIES_SURNAME));
            log.info("load: " + surnames.size() + " surnames");
            words = loadDictionary(ResourceUtils.getFile(dictionariesPath + "\\" + DICTIONARIES_WORDS));
            log.info("load: " + words.size() + " words");
        } catch (FileNotFoundException e) {
            log.error(e.toString());
        }

    }

    private List<String> loadDictionary(File file) {
        List<String> worlds = new ArrayList<>();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                worlds.add(line);
            }
            reader.close();
        } catch (IOException e) {
            log.error(e.toString());
        }
        return worlds;
    }

    private void fillOrders(int count) {
        for (int i = 0; i < count; i++) {
            Order order = generateOrder();
            orderRepository.add(order);
        }
    }

    private Order generateOrder() {
        Order order = new Order();
        order.setUser(getRandomUser());
        order.setSoldCertificates(getRandomSoldCertificates());
        order.setOrderDate(generateDate());
        order.setOrderPrice(generatePrice());
        return order;
    }

    private Set<SoldCertificate> getRandomSoldCertificates() {
        return Stream.generate(this::getRandomSoldCertificate)
                .limit(getRandomNumber(1, MAX_SOLD_CERTIFICATE_FOR_ORDER))
                .collect(Collectors.toSet());
    }

    private SoldCertificate getRandomSoldCertificate() {
        Certificate certificate = getRandomCertificate();
        return SoldCertificate.builder()
                .certificate(certificate)
                .soldPrice(certificate.getPrice())
                .discount(new BigDecimal(random.nextInt(100)))
                .count(getRandomNumber(1, MAX_COUNT_CERTIFICATE_FOR_SOLD))
                .build();
    }

    private Certificate getRandomCertificate() {
        return certificateRepository.findById(certificateIds.get(random.nextInt(certificateIds.size())));
    }

    private User getRandomUser() {
        return userRepository.findById(userIds.get(random.nextInt(userIds.size())));
    }

    private void fillCertificates(int count) {
        for (int i = 0; i < count; i++) {
            Certificate certificate = generateCertificate();
            certificateRepository.add(certificate);
            certificateIds.add(certificate.getId());
        }
    }

    private Certificate generateCertificate() {
        Certificate certificate = new Certificate();
        certificate.setName(generateCertificateName());
        certificate.setDescription(generateCertificateDescription());
        certificate.setDuration(generateCertificateDuration());
        certificate.setPrice(generatePrice());
        certificate.setTags(getRandomTags());
        return certificate;
    }

    private Timestamp generateDate() {
        long offset = Timestamp.valueOf(MIN_RANDOM_DATE).getTime();
        long end = Timestamp.valueOf(MAX_RANDOM_DATE).getTime();
        long diff = end - offset + 1;
        return new Timestamp(offset + (long) (Math.random() * diff));
    }

    private Set<Tag> getRandomTags() {
        Set<Tag> tags = new HashSet<>();
        for (int i = 0; i < getRandomNumber(CERTIFICATE_MIN_TAGS, CERTIFICATE_MAX_TAGS); i++) {
            Tag newTag;
            do {
                newTag = tagRepository.findById(tagIds.get(random.nextInt(tagIds.size())));
            } while (tags.contains(newTag));
            tags.add(tagRepository.findById(tagIds.get(random.nextInt(tagIds.size()))));
        }
        return tags;
    }

    private BigDecimal generatePrice() {
        return new BigDecimal(getRandomNumber(CERTIFICATE_MIN_PRICE, CERTIFICATE_MAX_PRICE));
    }

    private Long generateCertificateDuration() {
        return (long) getRandomNumber(CERTIFICATE_MIN_DURATION, CERTIFICATE_MAX_DURATION);
    }

    private String generateCertificateDescription() {
        StringBuilder description = new StringBuilder();
        int descriptionLength = getRandomNumber(CERTIFICATE_DESCRIPTION_MIN_LENGTH, CERTIFICATE_DESCRIPTION_MAX_LENGTH);
        for (int i = 0; i < descriptionLength; i++) {
            description.append(words.get(random.nextInt(words.size())));
            if (!(i + 1 >= descriptionLength))
                description.append(" ");
        }
        return description.toString();
    }

    private String generateCertificateName() {
        return words.get(random.nextInt(words.size()));
    }

    private void fillUsers(int count) {
        for (int i = 0; i < count; i++) {
            User user = generateUser();
            userRepository.add(user);
            userIds.add(user.getId());
        }
    }

    private User generateUser() {
        User user = new User();
        user.setLogin(generateLongin());
        user.setFirstname(firstnames.get(random.nextInt(firstnames.size())));
        user.setSurname(surnames.get(random.nextInt(surnames.size())));
        return user;
    }

    private String generateLongin() {
        String login;
        do {
            login = logins.get(random.nextInt(logins.size()));
        }
        while (usedLogins.contains(login));
        usedLogins.add(login);
        return login;
    }

    private void fillTags(int count) {
        for (int i = 0; i < count; i++) {
            Tag tag = generateTag();
            tagRepository.add(tag);
            tagIds.add(tag.getId());
        }
    }

    private Tag generateTag() {
        String name;
        do {
            name = words.get(random.nextInt(words.size()));
        }
        while (usedTagName.contains(name));
        usedTagName.add(name);
        Tag tag = new Tag();
        tag.setName(name);
        return tag;
    }

    private int getRandomNumber(int minElement, int maxElement) {
        return random.nextInt(maxElement - minElement + 1) + minElement;
    }
}