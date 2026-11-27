package missaomarte.servidor.api.dto;

import java.util.List;

public record PaginaPartidasResponse(List<PartidaResponse> itens, int pagina, int tamanho,
        long totalItens, int totalPaginas) {
}
