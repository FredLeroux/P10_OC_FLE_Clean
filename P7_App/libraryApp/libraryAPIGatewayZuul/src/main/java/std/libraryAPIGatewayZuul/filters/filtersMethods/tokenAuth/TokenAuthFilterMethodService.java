package std.libraryAPIGatewayZuul.filters.filtersMethods.tokenAuth;

public interface TokenAuthFilterMethodService {

	public void authTokenManagement(String urlRegex,String headerName,Integer size,Boolean isSymbolAccepted);

}
