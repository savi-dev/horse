// Copyright (C) 2012, The SAVI Project.
package ca.savi.glance.client;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import ca.savi.glance.model.GlanceClientDownloadRequest;
import ca.savi.glance.model.GlanceClientDownloadResponse;
import ca.savi.glance.model.GlanceClientGetInfoRequest;
import ca.savi.glance.model.GlanceClientGetInfoResponse;
import ca.savi.glance.model.GlanceClientRequest;
import ca.savi.glance.model.GlanceClientUploadRequest;
import ca.savi.glance.model.GlanceClientUploadResponse;
import ca.savi.glance.model.Imnode;
import ca.savi.glance.model.SaveImageToDisk;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

/**
 * This is GlanceClient.
 * @author Jie Yu Lin (Eric) <jieyu.lin@mail.utoronto.ca>
 * @version 0.1
 */
public class GlanceClient {
  protected Client glance_client;
  protected ClientConfig glance_cconfig;
  protected WebResource wr;
  protected final String DEFAULT_HOST = "http://localhost";
  protected final int DEFAULT_PORT = 9292;
  protected final String DEFAULT_DOC_ROOT = "/v1";
  protected final String AUTH_TOKEN = "x-auth-token";
  protected String doc_root;
  protected String auth_token;
  protected final int TRUNK_SIZE = 65536;

  public GlanceClient() {
    glance_cconfig = new DefaultClientConfig();
    glance_client = Client.create(glance_cconfig);
    URI glance_api_uri =
        UriBuilder.fromUri(DEFAULT_HOST).port(DEFAULT_PORT).build();
    wr = glance_client.resource(glance_api_uri);
  }

  public GlanceClient(URI host) {
    glance_cconfig = new DefaultClientConfig();
    glance_client = Client.create(glance_cconfig);
    URI glance_api_uri = UriBuilder.fromUri(host).port(DEFAULT_PORT).build();
    wr = glance_client.resource(glance_api_uri);
  }

  public GlanceClient(URI host, int port) {
    glance_cconfig = new DefaultClientConfig();
    glance_client = Client.create(glance_cconfig);
    URI glance_api_uri = UriBuilder.fromUri(host).port(port).build();
    wr = glance_client.resource(glance_api_uri);
  }

  public GlanceClient(URI host, int port, String client_config) {
    glance_cconfig = new DefaultClientConfig();
    glance_client = Client.create(glance_cconfig);
    URI glance_api_uri = UriBuilder.fromUri(host).port(port).build();
    wr = glance_client.resource(glance_api_uri);
    // TODO need to add client options for user to change client config
  }

  protected URI construct_uri_path(String action, String params) {
    return UriBuilder.fromUri(this.doc_root).path(action).path(params).build();
  }

  protected Builder config_wr_header_from_wr(GlanceClientRequest request,
      String auth_token, WebResource webR) {
    Builder wrb = webR.getRequestBuilder();
    return config_wr_header_from_wrb(request, auth_token, wrb);
  }

  protected Builder config_wr_header_from_wrb(GlanceClientRequest request,
      String auth_token, Builder wrb) {
    GlanceClientUploadRequest gcUploadRequest = request.getGcUploadRequest();
    if (GlanceClient_Util.isNotEmpty(auth_token)) {
      wrb = wrb.header(AUTH_TOKEN, auth_token);
    }
    if (gcUploadRequest != null) {
      wrb = gcUploadRequest.add_image_headers_to_webresource(wrb);
    }
    return wrb;
  }

  protected Builder config_wr_entity_from_wr(GlanceClientRequest request,
      WebResource webR) {
    Builder wrb = webR.getRequestBuilder();
    return config_wr_entity_from_wrb(request, wrb);
  }

  protected Builder config_wr_entity_from_wrb(GlanceClientRequest request,
      Builder wrb) {
    GlanceClientUploadRequest gcUploadRequest = request.getGcUploadRequest();
    if (gcUploadRequest != null) {
      InputStream entity_inputstream = gcUploadRequest.getImageInputStream();
      String imageLocalPath = gcUploadRequest.getImageLocalPath();
      byte[] imageByteArray = gcUploadRequest.getImageByteArray();
      String imageURI = gcUploadRequest.getImageURI();
      if (GlanceClient_Util.isNotEmpty(imageByteArray)) {
        wrb = wrb.entity(imageByteArray);
      } else if (entity_inputstream != null) {
        wrb =
            wrb.entity(entity_inputstream,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
      } else if (GlanceClient_Util.isNotEmpty(imageLocalPath)) {
        entity_inputstream =
            GlanceClient_Util.get_inputstream_from_local_file(imageLocalPath);
        wrb =
            wrb.entity(entity_inputstream,
                MediaType.APPLICATION_OCTET_STREAM_TYPE);
      } else if (GlanceClient_Util.isNotEmpty(imageURI)) {
        // TODO: download image file from given URI and build a Builder
        // with it
      }
      return wrb;
    } else {
      return wrb;
    }
  }

  protected Builder config_wr(String uri, GlanceClientRequest request,
      String auth_token) {
    Builder wrb;
    if (uri == null) {
      // throw new
      // GlanceClientException
      // ("URI not found: URI not found when configuring WebResource");
      System.out
          .println("URI not found: URI not found when configuring WebResource");
    }
    WebResource wr_temp = wr.path(uri);
    wrb = config_wr_entity_from_wr(request, wr_temp);
    if (wrb != null) {
      wrb = config_wr_header_from_wrb(request, auth_token, wrb);
    }
    return wrb;
  }

  public void GlanceCLConfigClient(String doc_root, String auth_token) {
    String selected_doc_root = DEFAULT_DOC_ROOT;
    if (GlanceClient_Util.isNotEmpty(doc_root)) {
      selected_doc_root = doc_root;
    }
    this.doc_root = selected_doc_root;
    this.auth_token = auth_token;
  }

  public void GlanceCLConfigClient(String doc_root) {
    String selected_doc_root = DEFAULT_DOC_ROOT;
    if (GlanceClient_Util.isNotEmpty(doc_root)) {
      selected_doc_root = doc_root;
    }
    this.doc_root = selected_doc_root;
  }

  protected boolean good_response_status(int code) {
    if (code == HttpURLConnection.HTTP_ACCEPTED
        || code == HttpURLConnection.HTTP_OK
        || code == HttpURLConnection.HTTP_CREATED
        || code == HttpURLConnection.HTTP_NO_CONTENT) {
      return true;
    } else {
      System.out.printf("Undesired HTTP status code:%d%n", code);
      return false;
    }
  }

  public GlanceClientGetInfoResponse GlanceCLGetImageInfo(
      GlanceClientGetInfoRequest gcGetInfoRequest) {
    GlanceClientGetInfoResponse gcGetInfoResponse;
    if (gcGetInfoRequest.getDetail()) {
      gcGetInfoResponse = get_public_images_detail_info();
    } else {
      gcGetInfoResponse = get_public_images_info();
    }
    return gcGetInfoResponse;
  }

  protected GlanceClientGetInfoResponse get_public_images_info() {
    String uri_path = construct_uri_path("/images", "").toString();
    GlanceClientGetInfoResponse gcGetInfoResponse =
        new GlanceClientGetInfoResponse();
    GlanceClientRequest request = new GlanceClientRequest();
    int responseStatusCode;
    String responseHeader;
    String responseEntity = "";
    try {
      ClientResponse response =
          config_wr(uri_path, request, auth_token).get(ClientResponse.class);
      responseStatusCode = response.getStatus();
      if (good_response_status(responseStatusCode)) {
        gcGetInfoResponse =
            response.getEntity(GlanceClientGetInfoResponse.class);
      } else {
        responseHeader = response.getHeaders().toString();
        if (response.hasEntity()) {
          responseEntity = response.getEntity(String.class);
        }
        System.out.println(responseHeader);
        System.out.println(responseEntity);
      }
    } catch (ClientHandlerException e) {
      System.out.println(e);
    } catch (Exception e) {
      System.out.println(e);
    }
    return gcGetInfoResponse;
  }

  protected GlanceClientGetInfoResponse get_public_images_detail_info() {
    String uri_path = construct_uri_path("/images/detail", "").toString();
    GlanceClientGetInfoResponse gcGetInfoResponse =
        new GlanceClientGetInfoResponse();
    GlanceClientRequest request = new GlanceClientRequest();
    int responseStatusCode;
    String responseHeader;
    String responseEntity = "";
    try {
      ClientResponse response =
          config_wr(uri_path, request, auth_token).get(ClientResponse.class);
      responseStatusCode = response.getStatus();
      if (good_response_status(responseStatusCode)) {
        gcGetInfoResponse =
            response.getEntity(GlanceClientGetInfoResponse.class);
      } else {
        responseHeader = response.getHeaders().toString();
        if (response.hasEntity()) {
          responseEntity = response.getEntity(String.class);
        }
        System.out.println(responseHeader);
        System.out.println(responseEntity);
      }
    } catch (ClientHandlerException e) {
      System.out.println(e);
    } catch (Exception e) {
      System.out.println(e);
    }
    return gcGetInfoResponse;
  }

  public void GlanceCLGetSpecficImageDetailInfo(String image_id) {
    String uri_path = construct_uri_path("/images/" + image_id, "").toString();
    GlanceClientRequest request = new GlanceClientRequest();
    int responseStatusCode;
    String responseHeader;
    String responseEntity = "";
    try {
      ClientResponse response = config_wr(uri_path, request, auth_token).head();
      responseStatusCode = response.getStatus();
      if (good_response_status(responseStatusCode)) {
        System.out.println(response.getEntity(String.class));
      } else {
        responseHeader = response.getHeaders().toString();
        if (response.hasEntity()) {
          responseEntity = response.getEntity(String.class);
        }
        System.out.println(responseHeader);
        System.out.println(responseEntity);
      }
    } catch (ClientHandlerException e) {
      System.out.println(e);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public GlanceClientDownloadResponse GlanceCLDownloadImage(
      GlanceClientDownloadRequest gcDownloadRequest) {
    GlanceClientDownloadResponse gcDownloadResponse =
        new GlanceClientDownloadResponse();
    GlanceClientRequest request = new GlanceClientRequest();
    request.setGcDownloadRequest(gcDownloadRequest);
    String image_id = gcDownloadRequest.getUuid();
    String image_local_path = gcDownloadRequest.getDonwloadLocalPath();
    String image_name = gcDownloadRequest.getImageName();
    boolean storeImageByteArray = gcDownloadRequest.getStoreByteArray();
    boolean saveToDisk = gcDownloadRequest.getSaveToDisk();
    boolean successful = false;
    String error = "";
    if (GlanceClient_Util.isEmpty(image_name)) {
      image_name = image_id;
    }
    int responseStatusCode;
    String responseHeader;
    String responseEntity = "";
    String uri_path = construct_uri_path("/images/" + image_id, "").toString();
    try {
      ClientResponse response =
          config_wr(uri_path, request, auth_token).get(ClientResponse.class);
      responseStatusCode = response.getStatus();
      if (good_response_status(responseStatusCode)) {
        if (storeImageByteArray) {
          System.out.println("Downloading ...");
          byte[] imageByteArray = response.getEntity(byte[].class);
          gcDownloadResponse.setImageByteArray(imageByteArray);
          successful = true;
          if (saveToDisk) {
            if (GlanceClient_Util.isNotEmpty(image_local_path)) {
              String dest_path = image_local_path + File.separator + image_name;
              SaveImageToDisk saveImageToDisk =
                  GlanceClient_Util.save_image_locally(imageByteArray,
                      dest_path);
              successful = saveImageToDisk.getSuccessful();
              error = saveImageToDisk.getError();
            } else {
              error =
                  "Local path can not be empty"
                      + "when saveToDisk option is selected";
              gcDownloadResponse.setIsSuccessful(successful);
              gcDownloadResponse.setError(error);
              return gcDownloadResponse;
            }
          }
        } else if (saveToDisk) {
          if (GlanceClient_Util.isNotEmpty(image_local_path)) {
            InputStream image_inputstream = response.getEntityInputStream();
            String dest_path = image_local_path + File.separator + image_name;
            SaveImageToDisk saveImageToDisk =
                GlanceClient_Util.save_image_locally(image_inputstream,
                    dest_path, TRUNK_SIZE);
            successful = saveImageToDisk.getSuccessful();
            error = saveImageToDisk.getError();
          } else {
            error =
                "Local path can not be empty when"
                    + "saveToDisk option is selected";
            gcDownloadResponse.setIsSuccessful(successful);
            gcDownloadResponse.setError(error);
            return gcDownloadResponse;
          }
        } else {
          gcDownloadResponse.setImageInputStream(response
              .getEntityInputStream());
          successful = true;
        }
      } else {
        responseHeader = response.getHeaders().toString();
        if (response.hasEntity()) {
          responseEntity = response.getEntity(String.class);
        }
        successful = false;
        error =
            "Error response code from Glance Server: " + responseHeader
                + responseEntity;
        System.out.println(responseHeader);
        System.out.println(responseEntity);
      }
    } catch (ClientHandlerException e) {
      System.out.println(e);
      gcDownloadResponse.setIsSuccessful(false);
      gcDownloadResponse.setError(e.toString());
      return gcDownloadResponse;
    } catch (Exception e) {
      System.out.println(e);
      gcDownloadResponse.setIsSuccessful(false);
      gcDownloadResponse.setError(e.toString());
      return gcDownloadResponse;
    }
    gcDownloadResponse.setIsSuccessful(successful);
    gcDownloadResponse.setError(error);
    return gcDownloadResponse;
  }

  public GlanceClientUploadResponse GlanceCLUploadImage(
      GlanceClientUploadRequest gcUploadRequest) {
    GlanceClientUploadResponse gcUploadResponse =
        new GlanceClientUploadResponse();
    String uri_path = construct_uri_path("/images", "").toString();
    GlanceClientRequest request = new GlanceClientRequest();
    request.setGcUploadRequest(gcUploadRequest);
    int responseStatusCode;
    String responseHeader;
    String responseEntity = "";
    try {
      Builder tempBuilder = config_wr(uri_path, request, auth_token);
      if (tempBuilder != null) {
        ClientResponse response = tempBuilder.post(ClientResponse.class);
        responseStatusCode = response.getStatus();
        if (good_response_status(responseStatusCode)) {
          gcUploadResponse =
              response.getEntity(GlanceClientUploadResponse.class);
        } else {
          responseHeader = response.getHeaders().toString();
          if (response.hasEntity()) {
            responseEntity = response.getEntity(String.class);
          }
          System.out.println(responseHeader);
          System.out.println(responseEntity);
        }
        gcUploadResponse.setUploadSuccessful(true);
      } else {
        gcUploadResponse.setUploadSuccessful(false);
      }
    } catch (ClientHandlerException e) {
      System.out.println(e);
    } catch (Exception e) {
      System.out.println(e);
    }
    return gcUploadResponse;
  }

  public void GlanceCLUpdateImage(GlanceClientUploadRequest gcUploadRequest) {
    String uri_path = construct_uri_path("/images", "").toString();
    GlanceClientRequest request = new GlanceClientRequest();
    request.setGcUploadRequest(gcUploadRequest);
    Imnode imnode = new Imnode();
    int responseStatusCode;
    String responseHeader;
    String responseEntity = "";
    try {
      ClientResponse response =
          config_wr(uri_path, request, auth_token).put(ClientResponse.class);
      responseStatusCode = response.getStatus();
      if (good_response_status(responseStatusCode)) {
        imnode = response.getEntity(Imnode.class);
        // System.out.println(imnode);
      } else {
        responseHeader = response.getHeaders().toString();
        if (response.hasEntity()) {
          responseEntity = response.getEntity(String.class);
        }
        System.out.println(responseHeader);
        System.out.println(responseEntity);
      }
    } catch (ClientHandlerException e) {
      System.out.println(e);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void GlanceCLRequestImageMembership(String image_id) {
    String uri_path =
        construct_uri_path("/images/" + image_id + "/members", "").toString();
    GlanceClientRequest request = new GlanceClientRequest();
    int responseStatusCode;
    String responseHeader;
    String responseEntity = "";
    try {
      ClientResponse response =
          config_wr(uri_path, request, auth_token).get(ClientResponse.class);
      responseStatusCode = response.getStatus();
      if (good_response_status(responseStatusCode)) {
        System.out.println(response.getEntity(String.class));
      } else {
        responseHeader = response.getHeaders().toString();
        if (response.hasEntity()) {
          responseEntity = response.getEntity(String.class);
        }
        System.out.println(responseHeader);
        System.out.println(responseEntity);
      }
    } catch (ClientHandlerException e) {
      System.out.println(e);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void GlanceCLRequestSharedImages(String tenant_name) {
    String uri_path =
        construct_uri_path("/shared-images/" + tenant_name, "").toString();
    GlanceClientRequest request = new GlanceClientRequest();
    int responseStatusCode;
    String responseHeader;
    String responseEntity = "";
    try {
      ClientResponse response =
          config_wr(uri_path, request, auth_token).get(ClientResponse.class);
      responseStatusCode = response.getStatus();
      if (good_response_status(responseStatusCode)) {
        System.out.println(response.getEntity(String.class));
      } else {
        responseHeader = response.getHeaders().toString();
        if (response.hasEntity()) {
          responseEntity = response.getEntity(String.class);
        }
        System.out.println(responseHeader);
        System.out.println(responseEntity);
      }
    } catch (ClientHandlerException e) {
      System.out.println(e);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void GlanceCLAddMemberToImage(String image_id, String tenant_name) {
    String uri_path =
        construct_uri_path("/images" + image_id + "/members/" + tenant_name, "")
            .toString();
    GlanceClientRequest request = new GlanceClientRequest();
    int responseStatusCode;
    String responseHeader;
    String responseEntity = "";
    try {
      ClientResponse response =
          config_wr(uri_path, request, auth_token).put(ClientResponse.class);
      responseStatusCode = response.getStatus();
      if (good_response_status(responseStatusCode)) {
        System.out.println(response.getEntity(String.class));
      } else {
        responseHeader = response.getHeaders().toString();
        if (response.hasEntity()) {
          responseEntity = response.getEntity(String.class);
        }
        System.out.println(responseHeader);
        System.out.println(responseEntity);
      }
    } catch (ClientHandlerException e) {
      System.out.println(e);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public void GlanceCLRemovingMemberFromImage(String image_id,
      String tenant_name) {
    String uri_path =
        construct_uri_path("/images/" + image_id + "/members/" + tenant_name,
            "").toString();
    GlanceClientRequest request = new GlanceClientRequest();
    int responseStatusCode;
    String responseHeader;
    String responseEntity = "";
    try {
      ClientResponse response =
          config_wr(uri_path, request, auth_token).delete(ClientResponse.class);
      responseStatusCode = response.getStatus();
      if (good_response_status(responseStatusCode)) {
        System.out.println(response.getEntity(String.class));
      } else {
        responseHeader = response.getHeaders().toString();
        if (response.hasEntity()) {
          responseEntity = response.getEntity(String.class);
        }
        System.out.println(responseHeader);
        System.out.println(responseEntity);
      }
    } catch (ClientHandlerException e) {
      System.out.println(e);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public String GlanceCLGetAuthToken(String username, String password,
      String tenantName, String keystoneHost) {
    DefaultClientConfig keystone_cconfig = new DefaultClientConfig();
    Client keystone_client = Client.create(keystone_cconfig);
    URI keystone_api_uri =
        UriBuilder.fromUri(keystoneHost).port(35357).path("v2.0")
            .path("tokens").build();
    WebResource keystoneWr = keystone_client.resource(keystone_api_uri);
    String requestBody =
        "{\"auth\":{\"tenantName\":\"" + tenantName
            + "\", \"passwordCredentials\":{\"username\": \"" + username
            + "\", \"password\": \"" + password + "\"}}}";
    ClientResponse response =
        keystoneWr.entity(requestBody, MediaType.APPLICATION_JSON_TYPE).post(
            ClientResponse.class);
    String rEntity = response.getEntity(String.class);
    int sIndex = rEntity.indexOf("\"id\"");
    int eIndex = rEntity.indexOf("\"tenant\"");
    String sub = rEntity.substring(sIndex + 7, eIndex - 3);
    this.auth_token = sub;
    return sub;
  }
}
