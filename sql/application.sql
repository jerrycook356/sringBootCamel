
CREATE TABLE ITEMS (
ITEM_I SERIAL,
SKU TEXT NOT NULL,
ITEM_DESCRIPTION TEXT DEFAULT NULL,
PRICE NUMERIC (5,2),
CRTE_TS TIMESTAMPTZ NULL DEFAULT current_timestamp
);


select * from items;

delete from items;

delete from items where sku ='100';
select * from items where sku = '100';