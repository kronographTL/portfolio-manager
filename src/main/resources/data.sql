drop table if exists ticker;

create table ticker
(ticker_id varchar(255) primary key,
    market_value double,
    share_name varchar(255),
    ticker_code varchar(255),
    expected_return double,
    annualized_standard_deviation double
);

insert into ticker (ticker_id, ticker_code, share_name, market_Value,expected_return,annualized_standard_deviation) values (1, 'TSLA', 'Tesla Inc.', 100.0,0.11,0.22);
insert into ticker (ticker_id, ticker_code, share_name, market_Value,expected_return,annualized_standard_deviation) values (2, 'ADYN', 'Adyen', 40.0,0.41,0.2);
insert into ticker (ticker_id, ticker_code, share_name, market_Value,expected_return,annualized_standard_deviation) values (3, 'MS', 'Morgan Stanley', 60.0,0.123,0.321);
insert into ticker (ticker_id, ticker_code, share_name, market_Value,expected_return,annualized_standard_deviation) values (4, 'GOGL', 'Alphabet Inc.', 90.0,0.411,0.144);
insert into ticker (ticker_id, ticker_code, share_name, market_Value,expected_return,annualized_standard_deviation) values (5, 'FB', 'Facebook Inc.', 81.0,0.298,0.341);
insert into ticker (ticker_id, ticker_code, share_name, market_Value,expected_return,annualized_standard_deviation) values (6, 'MST', 'Microsrft Corp.', 80.0,0.111,0.333);
insert into ticker (ticker_id, ticker_code, share_name, market_Value,expected_return,annualized_standard_deviation) values (7, 'APL', 'Apple', 88.0,0.4321,0.2987);


