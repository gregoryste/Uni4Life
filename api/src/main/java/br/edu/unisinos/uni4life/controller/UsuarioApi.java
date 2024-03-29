package br.edu.unisinos.uni4life.controller;

import java.util.UUID;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.edu.unisinos.uni4life.domain.Pagina;
import br.edu.unisinos.uni4life.dto.request.AtualizaUsuarioRequest;
import br.edu.unisinos.uni4life.dto.request.CadastraUsuarioRequest;
import br.edu.unisinos.uni4life.dto.response.UsuarioResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(tags = "UsuarioApi")
public interface UsuarioApi {

    @ApiOperation(value = "Operação responsável em consultar informações de um usuário.",
        notes = "Operação consulta os dados do usuário do identificador informado, "
            + "na base de dados. Esse endpoint é necessário estar <strong>autenticado</strong>")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Sucesso."),
        @ApiResponse(code = 401, message = "Usuário não autenticado."),
        @ApiResponse(code = 404, message = "Nenhum usuário não encontrado."),
        @ApiResponse(code = 500, message = "Erro Interno.")
    })
    UsuarioResponse consultar(@RequestParam("id") final UUID idUsuario);

    @ApiOperation(value = "Operação responsável por consultar os usuários que esta seguindo.",
        notes = "Operação consulta todos os usuários na qual o usuário autenticado esta seguindo na base de dados."
            + " Caso ele não esteja seguindo nenhum usuário é retornado então uma lista vazia."
            + " Esse endpoint é necessário estar <strong>autenticado</strong>")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Sucesso."),
        @ApiResponse(code = 401, message = "Usuário não autenticado."),
        @ApiResponse(code = 500, message = "Erro Interno.")
    })
    Pagina<UsuarioResponse> consultarUsuariosSeguidos(@RequestParam(value = "pagina", required = false) Integer pagina,
        @RequestParam(value = "tamanho", required = false) Integer tamanho);


    @ApiOperation(value = "Operação responsável por consultar os usuários para seguir.",
        notes = "Operação consulta todos os usuários na qual o usuário autenticado não esta seguindo."
            + " Caso ele não esteja seguindo todos os usuários é retornado então uma lista vazia."
            + " Esse endpoint é necessário estar <strong>autenticado</strong>")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Sucesso."),
        @ApiResponse(code = 401, message = "Usuário não autenticado."),
        @ApiResponse(code = 500, message = "Erro Interno.")
    })
    Pagina<UsuarioResponse> consultarUsuariosParaSeguir(@RequestParam(value = "pagina", required = false) Integer pagina,
        @RequestParam(value = "tamanho", required = false) Integer tamanho);

    @ApiOperation(value = "Operação responsável por cadastrar novo usuário.",
        notes = "Operação valida campos da requisição e após isso" +
            " cria novo registro na base de dados")
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Criado com sucesso."),
        @ApiResponse(code = 400, message = "<ul>\t\n"
            + "<li>Requisição inválida.</li>\t\n"
            + "<li>Esse endereço de email já foi cadastrado anteriormente.</li>\t\n"
            + "</ul>"),
        @ApiResponse(code = 500, message = "Erro interno.")
    })
    UsuarioResponse cadastrar(@RequestBody final CadastraUsuarioRequest request);


    @ApiOperation(value = "Operação responsável por atualizar dados do usuário.",
        notes = "Operação valida os campos informados da requisição e após isso" +
            " atualiza os dados do usuário logado com novo dados na base de dados.")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Atualizado com sucesso."),
        @ApiResponse(code = 400, message = "<ul>\t\n"
            + "<li>Requisição inválida.</li>\t\n"
            + "<li>Esse endereço de email já foi cadastrado anteriormente.</li>\t\n"
            + "</ul>"),
        @ApiResponse(code = 500, message = "Erro interno.")
    })
    UsuarioResponse atualizar(@RequestBody final AtualizaUsuarioRequest request);
}
