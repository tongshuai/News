package edu.feicui.mnewsupdate.volley.toolbox;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import edu.feicui.mnewsupdate.volley.AuthFailureError;
import edu.feicui.mnewsupdate.volley.NetworkResponse;
import edu.feicui.mnewsupdate.volley.Request;
import edu.feicui.mnewsupdate.volley.Response;
import edu.feicui.mnewsupdate.volley.Response.ErrorListener;
import edu.feicui.mnewsupdate.volley.Response.Listener;

public class MultiPosttRequest extends Request<String> {

    private MultipartEntity entity = new MultipartEntity();
    private final Listener<String> mListener;

    public MultiPosttRequest(String url, Listener<String> listener,
                             ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        // TODO Auto-generated constructor stub
        mListener = listener;
    }

    /**
     * 上传文件
     *
     * @param key       :描述
     * @param mFilePart :文件
     */
    public void buildMultipartEntity(String key, File mFilePart) {
        entity.addFilePart(key, mFilePart);
    }

    /**
     * 上传String
     *
     * @param key       :描述
     * @param mFilePart :文件
     */
    public void buildMultipartEntity(String key, String value) {
        entity.addStringPart(key, value);
    }

    @Override
    public String getBodyContentType() {
        return entity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            entity.writeTo(bos);
        } catch (IOException e) {
            e.getStackTrace();
        }
        return bos.toByteArray();
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return Response.success("Uploaded", getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

}
