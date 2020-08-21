drop table if exists ticker;

create table ticker
(ticker_id varchar(255) primary key,
    initial_market_value double,
    share_name varchar(255),
    ticker_code varchar(255)
);

insert into ticker (ticker_id, ticker_code, share_name, initial_Market_Value) values (1, 'TSLA', 'Tesla Inc.', 100.0);
insert into ticker (ticker_id, ticker_code, share_name, initial_Market_Value) values (2, 'ADYN', 'Adyen', 40.0);
insert into ticker (ticker_id, ticker_code, share_name, initial_Market_Value) values (3, 'MS', 'Morgan Stanley', 60.0);
insert into ticker (ticker_id, ticker_code, share_name, initial_Market_Value) values (4, 'GOGL', 'Alphabet Inc.', 90.0);
insert into ticker (ticker_id, ticker_code, share_name, initial_Market_Value) values (5, 'FB', 'Facebook Inc.', 81.0);
insert into ticker (ticker_id, ticker_code, share_name, initial_Market_Value) values (6, 'MST', 'Microsrft Corp.', 80.0);
insert into ticker (ticker_id, ticker_code, share_name, initial_Market_Value) values (7, 'APL', 'Apple', 88.0);


