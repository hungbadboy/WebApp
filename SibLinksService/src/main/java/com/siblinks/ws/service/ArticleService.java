package com.siblinks.ws.service;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.siblinks.ws.model.Article;
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

    /**
     * This method update number view artical
     * 
     * @param aid
     *            : Artical id
     * 
     * @return True update success, False no row update
     */
    ResponseEntity<Response> updateViewArticle(Article aid);
    /**
     * @param uid
     * @param arId
     * @return
     */
    ResponseEntity<Response> getUserRateArticle(long uid, long arId);

    /**
     * @param request
     * @return
     */
    ResponseEntity<Response> rateArticleAdmission(RequestData request);
}
