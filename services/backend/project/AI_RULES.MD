# Padrões de Código e Arquitetura - Sonar

Atue sempre seguindo estritamente estas regras:

1. ARQUITETURA (Clean Architecture)
- Entrypoint (Controllers): Só conhecem DTOs e UseCases. Nenhuma lógica de negócio aqui. Retornam sempre ResponseEntity.
- Application (UseCases): Contêm apenas a regra de negócio pura. Não conhecem HTTP, anotações do Spring Web ou Banco de Dados. Comunicam-se com o mundo externo via Interfaces (Gateways).
- Infrastructure (DataProviders/Gateways): Implementam as interfaces dos UseCases. Fazem chamadas a APIs (WebClient/RestTemplate) ou Banco de Dados (Repositories).

2. NOMENCLATURA E ESTILO
- Casos de uso devem terminar com `UseCase` (ex: `SincronizarNegocioUseCase`).
- Interfaces de saída devem terminar com `Gateway` (ex: `MoskitGateway`).
- Implementações de gateway devem terminar com `DataProvider` ou `ApiDataProvider`.
- Não utilize `@Autowired` em atributos, use sempre injeção por construtor (Lombok `@RequiredArgsConstructor` é permitido).

3. TRATAMENTO DE ERROS E MAPEAMENTO
- Não retorne exceções genéricas (`Exception`, `RuntimeException`). Crie e lance exceções de negócio ou de infraestrutura (ex: `DataProviderApiException`).
- Para conversão de objetos (Entity <-> Domain <-> DTO), utilize a abordagem padrão do projeto (ex: MapStruct ou classes Mapper estáticas).

4. NÃO UTILIZAR
- "@Data" não use @Data do lombok, sempre coloque o @Getter, @Setter, @Builder, @NoArgsConstructor, @AllArgsConstructor e o que precisar.