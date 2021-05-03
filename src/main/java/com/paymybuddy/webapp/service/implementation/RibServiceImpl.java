package com.paymybuddy.webapp.service.implementation;

import com.paymybuddy.webapp.model.PMBUser;
import com.paymybuddy.webapp.model.Rib;
import com.paymybuddy.webapp.repository.RibRepository;
import com.paymybuddy.webapp.service.RibService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RibServiceImpl implements RibService {

    @Autowired
    private RibRepository ribRepository;

    @Override
    public List<Rib> getRibsByUser(PMBUser user) {
        List<Rib> ribs = ribRepository.findAllByUser(user);
        return ribs;
    }

    @Override
    public Optional<Rib> getById(Long ribId) {
        return ribRepository.findById(ribId);
    }

    @Override
    public Optional<Rib> createRib() {
        return Optional.empty();
    }
}
