create table carts (
	id binary(16) default (uuid_to_bin(uuid())) not null primary key,
    date_created date default (curdate()) not null
);

create table cart_items (
  id bigint auto_increment primary key,
  cart_id binary(16) not null,
  product_id bigint not null,
  quantity int default 1 not null,
  foreign key (cart_id) references carts (id) on delete cascade,
  foreign key (product_id) references products (id) on delete cascade,
  unique key cart_items_cart_product_unique (cart_id, product_id)
);