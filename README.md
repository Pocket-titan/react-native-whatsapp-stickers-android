# react-native-whatsapp-stickers-android

## Getting started

`$ npm install react-native-whatsapp-stickers-android --save`

### Mostly automatic installation

`$ react-native link react-native-whatsapp-stickers-android`

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.whatsapp_stickers.RNWhatsappStickersPackage;` to the imports at the top of the file
  - Add `new RNWhatsappStickersPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-whatsapp-stickers-android'
  	project(':react-native-whatsapp-stickers-android').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-whatsapp-stickers-android/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-whatsapp-stickers-android')
  	```

## Usage
```javascript
import WhatsappStickers from 'react-native-whatsapp-stickers-android';

// TODO: What to do with the module?
WhatsappStickers;
```
  