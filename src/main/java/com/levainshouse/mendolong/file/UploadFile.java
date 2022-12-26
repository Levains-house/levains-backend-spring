package com.levainshouse.mendolong.file;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UploadFile {

    private final String storeFilename;
    private final String storeFileUrl;
}
