## CommentViewer 母艦 v0.1

### 実装済み
- SAF による TXT ファイル読み込み（Import TXT）
- WebSocket 接続 / 送信 / 受信（OkHttp）
- ViewModel + StateFlow によるコメント一覧表示
- LazyColumn でリアルタイム更新

### 動作確認
1. アプリ起動
2. Import TXT → ファイル選択 → 内容がコメント一覧に表示
3. WebSocket Connect → メッセージ受信で一覧に追加
4. Send ボタンで送信可能

### モジュール構成
- `core/network` : WebSocket クライアント雛形
- `core/storage` : SAF + TextFileReader
- `feature/commentviewer` : Screen + ViewModel
- `apps/app-commentviewer` : 起動エントリ

### 目的
配信コメントビューア母艦として  
TXTログ再生 + リアルタイムコメント統合 を行う基盤