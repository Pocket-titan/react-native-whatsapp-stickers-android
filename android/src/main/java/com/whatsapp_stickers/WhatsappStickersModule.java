package com.whatsapp_stickers;

import android.widget.Toast;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;

import java.util.ArrayList;

import com.whatsapp_stickers.stickers.WhitelistCheck;
import com.whatsapp_stickers.stickers.StickerContentProvider;
import com.whatsapp_stickers.stickers.StickerPackValidator;
import com.whatsapp_stickers.stickers.StickerPack;
import com.whatsapp_stickers.stickers.StickerPackLoader;

public class WhatsappStickersModule extends ReactContextBaseJavaModule {

    private static final int ADD_STICKER_PACK_REQUEST = 200;
    private static final String ADD_STICKER_PACK_ERROR = "Error adding sticker pack to WhatsApp";
    private static final String UNABLE_TO_FETCH_STICKER_PACKS = "Unable to fetch sticker packs";
    private static final String FETCH_STICKER_PACKS_ERROR = "Error fetching sticker packs";
    private final ReactApplicationContext reactContext;

    private Promise addStickerPackPromise;

    private final ActivityEventListener addStickerPackEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            if (requestCode == ADD_STICKER_PACK_REQUEST) {
                if (addStickerPackPromise != null) {
                    if (resultCode == Activity.RESULT_CANCELED) {
                        if (intent != null) {
                            final String validationError = intent.getStringExtra("validation_error");
                            if (validationError != null) {
                                // for dev eyes only
                                Log.e("AddStickerPackActivity", "Validation failed:" + validationError);
                            }
                        }
                        addStickerPackPromise.reject(ADD_STICKER_PACK_ERROR, "sticker_pack_not_added");
                    } else if (resultCode == Activity.RESULT_OK) {
                        addStickerPackPromise.resolve("Success");
                    }
                }
                addStickerPackPromise = null;
            }
        }
    };

    public WhatsappStickersModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        reactContext.addActivityEventListener(addStickerPackEventListener);
    }

    @Override
    public String getName() {
        return "WhatsappStickers";
    }

    // Showing a Toast message
    @ReactMethod
    public void showToast(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    // Adding a stickerPack
    @ReactMethod
    public void addStickerPack(String identifier, String stickerPackName, Promise promise) {
        Intent intent = new Intent();
        intent.setAction("com.whatsapp.intent.action.ENABLE_STICKER_PACK");
        intent.putExtra("sticker_pack_id", identifier);
        intent.putExtra("sticker_pack_authority", StickerContentProvider.authority);
        intent.putExtra("sticker_pack_name", stickerPackName);
        addStickerPackPromise = promise;
        try {
            Activity currentActivity = getCurrentActivity();
            currentActivity.startActivityForResult(intent, ADD_STICKER_PACK_REQUEST);
        } catch (ActivityNotFoundException e) {
            promise.reject(ADD_STICKER_PACK_ERROR, e);
        }
    }

    // Checking if a sticker pack is installed or not
    @ReactMethod
    public void isStickerPackInstalled(String identifier, Promise promise) {
        boolean isInstalled;
        try {
            isInstalled = WhitelistCheck.isWhitelisted(reactContext, identifier);
            promise.resolve(isInstalled);
        } catch(Exception e) {
            promise.reject(e);
        }
    }

    // Getting the installed stickers
    @ReactMethod
    public void fetchStickerPacks(Promise promise) {
        ArrayList<StickerPack> stickerPackList;
        // Convert to WritableArray
        WritableArray writableArrayStickerPackList = new WritableNativeArray();
        try {
            if (reactContext != null) {
                stickerPackList = StickerPackLoader.fetchStickerPacks(reactContext);
                for (StickerPack stickerPack : stickerPackList) {
                    StickerPackValidator.verifyStickerPackValidity(reactContext, stickerPack);
                    // Convert to WritableMap
                    writableArrayStickerPackList.pushMap(
                        stickerPack.toWritableMap()
                    );
                }
                promise.resolve(writableArrayStickerPackList);
            } else {
                promise.reject(FETCH_STICKER_PACKS_ERROR, "Could not fetch sticker packs");
            }
        } catch (Exception e) {
            promise.reject(UNABLE_TO_FETCH_STICKER_PACKS, e);
        }
    }
}