package com.levainshouse.mendolong.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.levainshouse.mendolong.exception.FileIoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileStore {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public UploadFile uploadFile(MultipartFile multipartFile) throws IOException {
        //원본 파일명
        String originalFilename = multipartFile.getOriginalFilename();
        //DB에 저장할 파일명
        String storeFilename = createStoreFilename(originalFilename);
        //파일을 지정된 로컬 위치에 저장
        File uploadFile = this.convert(multipartFile, storeFilename).orElseThrow(
                () -> new FileIoException("파일 저장에 실패하였습니다."));
        //s3로 업로드
        String uploadImageUrl = this.putS3(uploadFile, "static/" + storeFilename);
        //로컬에 있는 파일 삭제
        if(uploadFile.delete()) log.debug("Local file delete success");

        return new UploadFile(storeFilename, uploadImageUrl);
    }

    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    //TODO: UUID를 이용해서 DB에 저장할 파일명을 만든다.
    private String createStoreFilename(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    private Optional<File> convert(MultipartFile file, String storeFilename) throws IOException {
        File convertFile = new File(System.getProperty("user.dir") + "/" + storeFilename);
        if (convertFile.createNewFile()) { // 바로 위에서 지정한 경로에 File이 생성됨 (경로가 잘못되었다면 생성 불가능)
            try (FileOutputStream fos = new FileOutputStream(convertFile)) { // FileOutputStream 데이터를 파일에 바이트 스트림으로 저장하기 위함
                fos.write(file.getBytes());
            } catch(IOException e){
                throw new FileIoException("파일 저장에 실패하였습니다.");
            }
            return Optional.of(convertFile);
        }
        //파일을 저장하지 못했다면 빈 옵셔널 객체 반환
        return Optional.empty();
    }
}
