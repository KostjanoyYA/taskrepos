package ru.kostyanoy.testconfiguration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import ru.kostyanoy.entity.PenaltyEvent;
import ru.kostyanoy.repository.penaltyevents.PenaltyEventRepository;

import java.util.List;
import java.util.Optional;

@TestConfiguration
@EnableJpaRepositories
//@EntityScan("ru.kostyanoy.entity")
public class TestConfig {

    @Bean
    public PenaltyEventRepository restTemplateBuilder() {

        return new PenaltyEventRepository() {
            @Override
            public List<PenaltyEvent> find(Long stateNumberID) {
                return null;
            }

            @Override
            public List<PenaltyEvent> find(String firstName, String middleName, String lastName) {
                return null;
            }

            @Override
            public List<PenaltyEvent> findAll() {
                return null;
            }

            @Override
            public List<PenaltyEvent> findAll(Sort sort) {
                return null;
            }

            @Override
            public List<PenaltyEvent> findAllById(Iterable<Long> iterable) {
                return null;
            }

            @Override
            public <S extends PenaltyEvent> List<S> saveAll(Iterable<S> iterable) {
                return null;
            }

            @Override
            public void flush() {

            }

            @Override
            public <S extends PenaltyEvent> S saveAndFlush(S s) {
                return null;
            }

            @Override
            public void deleteInBatch(Iterable<PenaltyEvent> iterable) {

            }

            @Override
            public void deleteAllInBatch() {

            }

            @Override
            public PenaltyEvent getOne(Long aLong) {
                return null;
            }

            @Override
            public <S extends PenaltyEvent> List<S> findAll(Example<S> example) {
                return null;
            }

            @Override
            public <S extends PenaltyEvent> List<S> findAll(Example<S> example, Sort sort) {
                return null;
            }

            @Override
            public Page<PenaltyEvent> findAll(Pageable pageable) {
                return null;
            }

            @Override
            public <S extends PenaltyEvent> S save(S s) {
                return null;
            }

            @Override
            public Optional<PenaltyEvent> findById(Long aLong) {
                return Optional.empty();
            }

            @Override
            public boolean existsById(Long aLong) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }

            @Override
            public void deleteById(Long aLong) {

            }

            @Override
            public void delete(PenaltyEvent penaltyEvent) {

            }

            @Override
            public void deleteAll(Iterable<? extends PenaltyEvent> iterable) {

            }

            @Override
            public void deleteAll() {

            }

            @Override
            public <S extends PenaltyEvent> Optional<S> findOne(Example<S> example) {
                return Optional.empty();
            }

            @Override
            public <S extends PenaltyEvent> Page<S> findAll(Example<S> example, Pageable pageable) {
                return null;
            }

            @Override
            public <S extends PenaltyEvent> long count(Example<S> example) {
                return 0;
            }

            @Override
            public <S extends PenaltyEvent> boolean exists(Example<S> example) {
                return false;
            }
        };
    }
}
