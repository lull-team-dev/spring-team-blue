-- ユーザー初期データ
INSERT INTO users (name, email, password, profile, address, tel) VALUES
('こたつ', 'kotatu@example.com', 'kotatu', '制作者A', '東京都', '090-1111-1111'),
('みかん', 'mikan@example.com', 'mikan', '購入者A', 'ブラジル', '090-1111-2222'),
('さわら', 'sawara@example.com', 'sawara', '購入者B', '石川県', '090-1111-3333');

-- カテゴリー初期データ
INSERT INTO categories (name) VALUES
('アクセサリー'), ('バッグ・財布'), ('雑貨'), ('その他');

-- 商品初期データ
INSERT INTO items (user_id, category_id, name, image, memo, price, sold_out) VALUES
(1, 1, 'ネックレス', 'necklace1.jpg', '天然石を使った手作りです', 2500, TRUE),
(1, 1, 'レジンピアス', 'resin_pierce.jpg', '春カラーのレジンアクセサリー', 1800, FALSE),
(1, 2, '刺繍ポーチ', 'embroidery.jpg', 'ハンドステッチで仕上げました', 1200, TRUE),
(2, 3, '木製スマホスタンド', 'wood_stand.jpg', '麻糸で編んだコースターセット', 1500, FALSE),
(2, 3, 'キャンドル', 'candle.jpg', '香りが広がる癒しのキャンドル', 1300, TRUE),
(2, 3, '手編みコースター', 'knitted_coaster.jpg', '麻糸で編んだコースターセット', 900, FALSE),
(3, 3, '革のキーケース', 'leather_keycase.jpg', '花柄刺繍入りのポーチです', 3200, TRUE),
(3, 3, 'ドライフラワーリース', 'dryflower_wreath.jpg', 'インテリアにぴったりです', 2700, FALSE),
(3, 3, 'ビーズブレスレット', 'beads_bracelet.jpg', 'カラフルなビーズを使ってます', 1600, TRUE);
