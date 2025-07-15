
-- ユーザー初期データ
INSERT INTO users (name, nickname, email, password, profile, zip, prefecture, city, town, building, tel, payment_method) VALUES
('温水冬華', 'こたつ', 'kotatu@example.com', 'kotatu', '子育ての合間に、手作りアクセサリー作ってます！', '123-4567', '東京都', '港区', '赤坂1-2-3', 'コーポ101', '090-1111-1111', 'クレジット'),
('鈴木・オランジュ・花梨', 'みかん', 'mikan@example.com', 'mikan', '手作りはしません。購入のみで利用しています。', '987-6543', 'ブラジル州', 'サンパウロ市', '中央通り5', '', '090-1111-2222', '代金引換'),
('魚沢紗良', 'さわら', 'sawara@example.com', 'sawara', 'ホーム雑貨中心に作っています！時々気に入ったものを購入したり・・・', '456-7890', '石川県', '金沢市', '東山', 'アパート202', '090-1111-3333', 'クレジット'),
('金城美海', 'Gold Marin', 'kane@example.com', 'subeteKane', '本業が調香師です。副業でアロマキャンドル販売中。', '321-0000', '大阪府', '大阪市', '北区梅田', 'ビル8F', '080-1111-4444', 'クレジット');

-- カテゴリー初期データ
INSERT INTO categories (name) VALUES
('アクセサリー'), ('バッグ・財布'), ('雑貨'), ('キッズ'), ('その他');

-- 商品初期データ
INSERT INTO items (user_id, category_id, name, image, memo, price, sold_out) VALUES
(1, 1, 'ネックレス', 'necklace1.png', '天然石を使った手作りです', 2500, TRUE),
(1, 1, 'レジンピアス', 'resin_pierce.png', '春カラーのレジンアクセサリー', 1800, FALSE),
(1, 2, '刺繍ポーチ', 'embroidery.png', 'ハンドステッチで仕上げました', 1200, TRUE),
(2, 3, '木製スマホスタンド', 'wood_stand.png', '麻糸で編んだコースターセット', 1500, FALSE),
(2, 3, 'キャンドル', 'candle.png', '香りが広がる癒しのキャンドル', 1300, TRUE),
(2, 3, '手編みコースター', 'knitted_coaster.png', '麻糸で編んだコースターセット', 900, FALSE),
(3, 3, '革のキーケース', 'leather_keycase.png', '花柄刺繍入りのポーチです', 3200, TRUE),
(3, 3, 'ドライフラワーリース', 'dryflower_wreath.png', 'インテリアにぴったりです', 2700, FALSE),
(3, 3, 'ビーズブレスレット', 'beads_bracelet.png', 'カラフルなビーズを使ってます', 1600, TRUE);
