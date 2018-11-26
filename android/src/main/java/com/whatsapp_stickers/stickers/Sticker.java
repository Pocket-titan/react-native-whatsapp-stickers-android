/*
 * Copyright (c) WhatsApp Inc. and its affiliates.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.whatsapp_stickers.stickers;

import android.os.Parcel;
import android.os.Parcelable;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableNativeArray;

import java.util.List;

class Sticker implements Parcelable {
    String imageFileName;
    List<String> emojis;
    long size;

    Sticker(String imageFileName, List<String> emojis) {
        this.imageFileName = imageFileName;
        this.emojis = emojis;
    }

    protected Sticker(Parcel in) {
        imageFileName = in.readString();
        emojis = in.createStringArrayList();
        size = in.readLong();
    }

    public static final Creator<Sticker> CREATOR = new Creator<Sticker>() {
        @Override
        public Sticker createFromParcel(Parcel in) {
            return new Sticker(in);
        }

        @Override
        public Sticker[] newArray(int size) {
            return new Sticker[size];
        }
    };

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public WritableMap toWritableMap() {
        WritableMap writableSticker = new WritableNativeMap();
        writableSticker.putString("imageFileName", imageFileName);

        WritableArray writableEmojis = new WritableNativeArray();
        for (String emoji : emojis) {
            writableEmojis.pushString(emoji);
        }
        writableSticker.putArray("emojis", writableEmojis);
        writableSticker.putDouble("size", (double) size);

        return writableSticker;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageFileName);
        dest.writeStringList(emojis);
        dest.writeLong(size);
    }
}