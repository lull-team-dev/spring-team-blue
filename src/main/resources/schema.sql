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
DROP TABLE IF EXISTS messages CASCADE;

-- ユーザーテーブル
CREATE TABLE users (
id SERIAL PRIMARY KEY,
name TEXT NOT NULL,
nickname TEXT NOT NULL,
email TEXT NOT NULL UNIQUE,
password TEXT NOT NULL,
profile TEXT,
zip VARCHAR(8),               
prefecture TEXT,              
city TEXT,                    
town TEXT,                    
building TEXT,            
tel TEXT,
payment_method VARCHAR(50),
icon TEXT
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
  reviewer_id INTEGER NOT NULL REFERENCES users(id),        -- 書いた人
  reviewee_id INTEGER NOT NULL REFERENCES users(id),        -- 書かれた人
  score SMALLINT NOT NULL CHECK (score BETWEEN 1 AND 5),    -- 星1〜5
  review_text TEXT,
  review_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ブックマークテーブル
CREATE TABLE bookmarks (
    id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    item_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- チャットルームテーブル
CREATE TABLE chats (
  id SERIAL PRIMARY KEY,
  item_id INTEGER NOT NULL,
  client_id INTEGER NOT NULL,
  owner_id INTEGER NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (item_id) REFERENCES items(id),
  FOREIGN KEY (client_id) REFERENCES users(id),
  FOREIGN KEY (owner_id) REFERENCES users(id)
);

--　チャットメッセージテーブル
CREATE TABLE messages (
  id SERIAL PRIMARY KEY,
  chat_id INTEGER NOT NULL,
  sender_id INTEGER NOT NULL,
  message TEXT NOT NULL,
  sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  is_read BOOLEAN DEFAULT false,
  FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE,
  FOREIGN KEY (sender_id) REFERENCES users(id)
);


-- フォローテーブル
CREATE TABLE follows (
  id SERIAL PRIMARY KEY,
  follower_id INTEGER NOT NULL,
  followed_id INTEGER NOT NULL,
  followed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (follower_id) REFERENCES users(id),
  FOREIGN KEY (followed_id) REFERENCES users(id),
  UNIQUE (follower_id, followed_id)
);
