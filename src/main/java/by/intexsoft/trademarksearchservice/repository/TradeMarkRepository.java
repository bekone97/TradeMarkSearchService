package by.intexsoft.trademarksearchservice.repository;

import by.intexsoft.trademarksearchservice.model.TradeMark;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeMarkRepository extends MongoRepository<TradeMark, String> {
    Page<TradeMark> findAllByMarkFeature(String markFeature, Pageable pageable);

    Page<TradeMark> findAllByMarkFeature(String markFeature, TextCriteria textCriteria, Pageable pageable);

}
