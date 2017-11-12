# battlefield
とあるじゃんけん学習プログラム課題のバトル用プログラム。  
指定のライブラリを用いたクラスのjarファイルを選択することでじゃんけんできる。  
バトル用プログラム`field`は動くようにはなっているが、まだ実装中...

### フォルダ階層について
全てgradleプロジェクトで書かれています。

|フォルダ|説明|
|---|---|
field|対戦環境のGUI
lib|対戦用プログラムのライブラリ
sample|ライブラリを使ったプログラムサンプル

### 使い方
libプロジェクトをjarライブラリとしてビルドして下さい。jarファイルは`/build/libs`にあります。ビルドするのが面倒な場合が[Relase](https://github.com/Khromium/battlefield/releases)から取ってきても構いません。

```
$ ./gradlew jar
```

ビルドしたプロジェクトを自分のプロジェクトに取り込みます。
sampleを用いる場合は　`sample/libs` に配置して下さい。

プロジェクト内に `Engine` クラスを作成し `RCSListener` をimplementsして下さい。
アルゴリズムを一通り書き終わった上記と同様のコマンドでjarファイルを作成して下さい。

```
$ ./gradlew jar
```


詳しい実装方法は [sample](https://github.com/Khromium/battlefield/blob/master/sample/src/main/java/rps/Engine.java) を確認して下さい

実際の対戦用プログラムでテストする場合もライブラリを配置する必要があります。 `field/libs` に配置して下さい。
配置したら以下のコマンドでGUIが起動するのでそれぞれjarファイルを選択して実行ボタンを押せば動きます。

```
$ ./gradlew run
```
