package ru.itis.dis403.spring.security.service;

import org.springframework.stereotype.Service;
import ru.itis.dis403.spring.security.model.Phone;
import ru.itis.dis403.spring.security.repository.PhoneRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PhoneService {
    private final PhoneRepository phoneRepository;

    public PhoneService(PhoneRepository phoneRepository) {
        this.phoneRepository = phoneRepository;
    }

    public Phone save(Phone phone) {
        return phoneRepository.save(phone);
    }

    public List<Phone> findAll() {
        return phoneRepository.findAll();
    }

    public Optional<Phone> findById(Long id) {
        return phoneRepository.findById(id);
    }

    public List<Phone> getPhoneLike(String num) {
        return phoneRepository.getPhoneLike(num);
    }
}