package missaomarte.infrastructure.ranking;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import missaomarte.application.Dificuldade;
import missaomarte.application.RankingEntry;
import missaomarte.application.RankingException;
import missaomarte.application.RankingRepository;

/** Ranking em arquivo JSON, escrito e lido à mão, no formato da Fase 0. */
public class FileRankingRepository implements RankingRepository {

    private final Path caminho;

    public FileRankingRepository(Path caminho) {
        this.caminho = caminho;
    }

    @Override
    public List<RankingEntry> carregar() {
        if (!Files.exists(caminho)) {
            return new ArrayList<>();
        }
        try {
            String json = new String(Files.readAllBytes(caminho), StandardCharsets.UTF_8).trim();
            return interpretar(json);
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void salvar(List<RankingEntry> entradas) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < entradas.size(); i++) {
            RankingEntry entry = entradas.get(i);
            builder.append("{\"name\":\"")
                    .append(entry.nome().replace("\"", "\\\""))
                    .append("\",\"score\":")
                    .append(entry.pontuacao())
                    .append(",\"dificuldade\":\"")
                    .append(entry.dificuldade().name())
                    .append("\",\"passageirosColetados\":")
                    .append(entry.passageirosColetados())
                    .append(",\"dataHora\":\"")
                    .append(entry.dataHora())
                    .append("\",\"tempoJogo\":")
                    .append(entry.tempoJogo())
                    .append("}");
            if (i < entradas.size() - 1) {
                builder.append(",");
            }
        }
        builder.append("]");
        try {
            Files.write(caminho, builder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RankingException(e.getMessage(), e);
        }
    }

    @Override
    public void limpar() {
        try {
            Files.deleteIfExists(caminho);
        } catch (IOException e) {
            throw new RankingException(e.getMessage(), e);
        }
    }

    // Mesmo parser da Fase 0: nome com vírgula é descartado (ponto b).
    private static List<RankingEntry> interpretar(String json) {
        List<RankingEntry> ranking = new ArrayList<>();
        if (json.isEmpty() || json.equals("[]")) {
            return ranking;
        }
        json = json.trim();
        if (json.startsWith("[")) {
            json = json.substring(1);
        }
        if (json.endsWith("]")) {
            json = json.substring(0, json.length() - 1);
        }

        int index = 0;
        while (index < json.length()) {
            int start = json.indexOf('{', index);
            if (start < 0) break;
            int end = json.indexOf('}', start);
            if (end < 0) break;
            String object = json.substring(start + 1, end);
            String name = null;
            Integer score = null;
            Dificuldade dificuldade = Dificuldade.MEDIO;
            int passageirosColetados = 0;
            String dataHora = "";
            long tempoJogo = 0;

            for (String part : object.split(",")) {
                String[] pair = part.split(":", 2);
                if (pair.length != 2) continue;
                String key = pair[0].trim().replaceAll("\"", "");
                String value = pair[1].trim();

                if (key.equals("name")) {
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        name = value.substring(1, value.length() - 1).replace("\\\"", "\"");
                    }
                } else if (key.equals("score")) {
                    try {
                        score = Integer.parseInt(value);
                    } catch (NumberFormatException ignored) {
                        // valor ilegível: a entrada é descartada abaixo
                    }
                } else if (key.equals("dificuldade")) {
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        dificuldade = Dificuldade.deString(value.substring(1, value.length() - 1));
                    }
                } else if (key.equals("passageirosColetados")) {
                    try {
                        passageirosColetados = Integer.parseInt(value);
                    } catch (NumberFormatException ignored) {
                        // mantém 0
                    }
                } else if (key.equals("dataHora")) {
                    if (value.startsWith("\"") && value.endsWith("\"")) {
                        dataHora = value.substring(1, value.length() - 1);
                    }
                } else if (key.equals("tempoJogo")) {
                    try {
                        tempoJogo = Long.parseLong(value);
                    } catch (NumberFormatException ignored) {
                        // mantém 0
                    }
                }
            }
            if (name != null && score != null) {
                ranking.add(new RankingEntry(name, score, dificuldade, passageirosColetados, dataHora, tempoJogo));
            }
            index = end + 1;
        }

        ranking.sort(Comparator.comparingInt(RankingEntry::pontuacao).reversed());
        return ranking;
    }
}
