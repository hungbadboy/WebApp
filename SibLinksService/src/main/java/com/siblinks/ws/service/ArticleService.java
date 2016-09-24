package com.siblinks.ws.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.model.RequestData;
import com.siblinks.ws.response.Response;

public interface ArticleService {

	public ResponseEntity<Response> getArticles(RequestData request);

	public ResponseEntity<Response> getArticleDetail(RequestData request);

	public ResponseEntity<Response> getArticleCommentsPN(RequestData request);

    public ResponseEntity<Response> getAllArticles();

	public ResponseEntity<Response> deleteArticle(RequestData request);

	public ResponseEntity<Response> updateArticle(RequestData request);

	public ResponseEntity<Response> uploadFile(MultipartFile uploadfile)
			throws IOException;

	public ResponseEntity<Response> createArticle(RequestData request) throws FileNotFoundException;

	public ResponseEntity<byte[]> getImageArticle(String arId) throws IOException;

	public ResponseEntity<Response> getArticleByUserPN(RequestData request);
    /**
     * @param idAdmission
     * @return
     */
    public ResponseEntity<Response> getArticleAdmission(String idAdmission);
}
