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
3. Insert the following lines inside `android/app/build.gradle`:
```
android {
	...
	defaultConfig {
		...
		def contentProviderAuthority = applicationId + ".stickercontentprovider"
		// Creates a placeholder property to use in the manifest.
		manifestPlaceholders =
			[contentProviderAuthority: contentProviderAuthority]
		// Adds a new field for the authority to the BuildConfig class.
		buildConfigField(
			"String",
			"CONTENT_PROVIDER_AUTHORITY",
			"\"${contentProviderAuthority}\""
		)
	}
}
...
dependencies {
	...
	compile project(':react-native-whatsapp-stickers-android')
}
```

## Usage

First put your stickers in `android/app/src/main/assets/<STICKER_PACK_IDENTIFIER_HERE>` and list the pack in `android/app/src/main/assets/contents.json`, according to the sticker pack documentation provided by WhatsApp found [here](https://github.com/WhatsApp/stickers/tree/master/Android#modifying-the-contentsjson-file). Also read the sticker requirements provided by WhatsApp in the same repo. If in doubt about implementation, take a look at the example app provided in the GitHub repo for inspiration.

```javascript
import WhatsappStickers from 'react-native-whatsapp-stickers-android';

// addStickerPack :: (identifier, name) -> Promise () Error
WhatsappStickers.addStickerPack(identifier, name)
	.then(() => console.log(`Successfully added sticker pack ${name} to WhatsApp!`))
	.catch(error => console.error(`Error adding sticker pack ${name}`, error))

// isStickerPackInstalled :: identifier -> Promise Boolean Error
WhatsappStickers.isStickerPackInstalled(identifier)
	.then(installed => this.setState({ installed }))
	.catch(error => console.error(error))

// fetchStickerPacks :: () -> Promise [StickerPack] Error
WhatsappStickers.fetchStickerPacks()
	.then(stickerPackList => {
		// contents.json can have multiple sticker packs, so this is an array
		let firstStickerPack = stickerPackList[0] || {}
		this.setState({ stickers: firstStickerPack.stickers })
	})
	.catch(error => console.error(error))

/*  Types of StickerPack and Sticker

StickerPack :: {
  identifier: String,
  name: String,
  publisher: String,
  trayImageFile: String,
  publisherEmail: String,
  publisherWebsite: String,
  privacyPolicyWebsite: String,
  licenseAgreementWebsite: String,
  iosAppStoreLink: String,
  androidPlayStoreLink: String,
  totalSize: Number,
  isWhitelisted: Boolean,
  stickers: [Sticker],
}

Sticker :: {
	imageFileName: String,
  size: Number,
  emojis: [String],
}
*/
```

## Issues or suggestions

If you run into problems or have a feature suggestion, feel free to make an issue on the repo or send me a personal message.
  
## Further reading

This repo was ported from the [WhatsApp Sticker Example Android app](https://github.com/WhatsApp/stickers/tree/master/Android). You can find answers to sticker-related questions there, such as sticker format, size, and other requirements.
