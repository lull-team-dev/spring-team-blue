-- 各種テーブル削除
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS history CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS categories CASCADE;

-- ユーザーテーブル
CREATE TABLE users (
id SERIAL PRIMARY KEY,
name TEXT NOT NULL,
nickname TEXT NOT NULL,
email TEXT NOT NULL UNIQUE,
password TEXT NOT NULL,
profile TEXT,
address TEXT,
tel TEXT
);

-- カテゴリーテーブル
CREATE TABLE categories (
id SERIAL PRIMARY KEY,
name VARCHAR(50) NOT NULL
);

-- 商品テーブル
CREATE TABLE items (
id SERIAL PRIMARY KEY,
user_id INTEGER NOT NULL REFERENCES users(id),
category_id INTEGER NOT NULL REFERENCES categories(id),
name TEXT NOT NULL,
image TEXT NOT NULL,
memo TEXT NOT NULL,
price INTEGER NOT NULL CHECK (price >= 0),
sold_out BOOLEAN NOT NULL
);

-- 注文テーブル
CREATE TABLE orders (
id SERIAL PRIMARY KEY,
consumer_id INTEGER NOT NULL REFERENCES users(id),
order_date TIMESTAMP,
total_price INTEGER NOT NULL CHECK (total_price >= 0),
status SMALLINT,
delivery_address TEXT NOT NULL,
delivery_tel TEXT
);

-- 購入履歴テーブル
CREATE TABLE history (
id SERIAL PRIMARY KEY,
user_id INTEGER REFERENCES users(id),
item_id INTEGER REFERENCES items(id),
date TIMESTAMP NOT NULL,
total_price INTEGER NOT NULL
);

-- レビューテーブル
CREATE TABLE reviews (
id SERIAL PRIMARY KEY,
items_id INTEGER NOT NULL REFERENCES items(id),
user_id INTEGER NOT NULL REFERENCES users(id),
score SMALLINT NOT NULL CHECK (score >= 1 AND score <= 5),
review_text TEXT NOT NULL,
review_date TIMESTAMP NOT NULL
);
