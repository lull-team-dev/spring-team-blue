-- 各種テーブル削除
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS orders CASCADE;
DROP TABLE IF EXISTS history CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS bookmarks CASCADE;
DROP TABLE IF EXISTS chats CASCADE;
DROP TABLE IF EXISTS follows CASCADE;

-- ユーザーテーブル
CREATE TABLE users (
id SERIAL PRIMARY KEY,
name TEXT NOT NULL,
nickname TEXT NOT NULL,
email TEXT NOT NULL UNIQUE,
password TEXT NOT NULL,
profile TEXT,
address TEXT,
tel TEXT,
payment_method VARCHAR(50) 
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
item_id INTEGER NOT NULL REFERENCES items(id),
order_date TIMESTAMP,
total_price INTEGER NOT NULL CHECK (total_price >= 0),
status SMALLINT,
postal_code VARCHAR(10), 
delivery_address TEXT NOT NULL,
delivery_tel TEXT,
payment_method VARCHAR(50) NOT NULL
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

-- ブックマークテーブル
CREATE TABLE bookmarks (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    item_id INT NOT NULL,
    item_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- チャットテーブル
CREATE TABLE chats (
  id SERIAL PRIMARY KEY,
  sender_id INT NOT NULL,
  receiver_id INT NOT NULL,
  message TEXT NOT NULL,
  sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (sender_id) REFERENCES users(id),
  FOREIGN KEY (receiver_id) REFERENCES users(id)
);

-- フォローテーブル
CREATE TABLE follows (
  id SERIAL PRIMARY KEY,
  follower_id INT NOT NULL,
  followed_id INT NOT NULL,
  followed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (follower_id) REFERENCES users(id),
  FOREIGN KEY (followed_id) REFERENCES users(id),
  UNIQUE (follower_id, followed_id)
);
