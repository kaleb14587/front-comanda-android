package com.sistemltda.vyper.vyper.VyperServerAPI.Endpoints;

import com.sistemltda.vyper.vyper.VyperServerAPI.Forms.AddItemComanda;
import com.sistemltda.vyper.vyper.VyperServerAPI.Forms.CredentialsVyper;
import com.sistemltda.vyper.vyper.VyperServerAPI.Forms.FormCliente;
import com.sistemltda.vyper.vyper.VyperServerAPI.Forms.FormPagamento;
import com.sistemltda.vyper.vyper.VyperServerAPI.Forms.FormRegistro;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.Authenticate;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.ClienteComanda;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.CompEmail;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.RegistroRetorno;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.ResultadoAddItem;
import com.sistemltda.vyper.vyper.VyperServerAPI.Retornos.ResultadoPagamento;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface DataServiceEndpoints {
//    @Part("grant_type") String grant_type,
//    @Part("client_id")String client_id,
//    @Part("client_secret") String client_secret,
//    @Part("username") String username,
//    @Part("password") String password,
//    @Part("scope") String scope
    @Headers("Content-Type: application/json")
    @POST("oauth/token")
    Call<Authenticate> getAccessToken(@Body CredentialsVyper credential);
    @POST("api/registrar-dispositivo/{user}")
    Call<RegistroRetorno> registraDispositivo(@Header("Authorization")String bearer,@Path("user") String user, @Body FormRegistro form);
    @POST("api/valida-cliente")
    Call<ClienteComanda> validaCliente(@Header("Authorization")String bearer, @Body FormCliente form);
    @POST("api/registra-cliente")
    Call<ClienteComanda> registracliente(@Header("Authorization")String bearerr, @Body FormCliente form);
    @POST("api/adiciona-items")
    Call<ResultadoAddItem> adicionaItensComanda(@Header("Authorization")String bearerr, @Body AddItemComanda form);
    @POST("api/fecha-comanda")
    Call<ResultadoPagamento> pagaComanda(@Header("Authorization")String bearerr, @Body FormPagamento form);

    @FormUrlEncoded
    @POST("api/comprovante/{id_comanda}")
    Call<CompEmail> comprovantePEmail(@Header("Authorization")String bearerr, @Path("id_comanda") String id_comanda, @Field("registroVyper") String registroVyper);
}
