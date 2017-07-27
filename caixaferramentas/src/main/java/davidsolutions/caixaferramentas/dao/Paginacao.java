package davidsolutions.caixaferramentas.dao;

public class Paginacao {

	private static final int QUANTIDADE_REGISTRO_PAGINA = 5;
	private int quantidadeRegistroPagina;
	private int numeroPagina;
	private int posicaoInicialPagina;
	private int posicaoFinalPagina;
	private int totalRegistros;
	private int totalPaginas;

	public Paginacao() {
		this.quantidadeRegistroPagina = QUANTIDADE_REGISTRO_PAGINA;
		this.setNumeroPagina(1);
	}

	public void setQuantidadeRegistroPagina(Integer quantidadeRegistroPagina) {

		if (quantidadeRegistroPagina == null || quantidadeRegistroPagina <= 0) {
			return;
		}
		this.quantidadeRegistroPagina = quantidadeRegistroPagina;
		this.setNumeroPagina(this.numeroPagina);
	}

	public Integer getQuantidadeRegistroPagina() {
		return this.quantidadeRegistroPagina;
	}

	public void setNumeroPagina(Integer numeroPagina) {

		if (numeroPagina == null || numeroPagina <= 0) {
			return;
		}
		this.numeroPagina = numeroPagina;
		this.posicaoFinalPagina = this.numeroPagina * this.quantidadeRegistroPagina;
		this.posicaoInicialPagina = this.posicaoFinalPagina - this.quantidadeRegistroPagina + 1;
	}

	public Integer getNumeroPagina() {
		return this.numeroPagina;
	}

	public int getPosicaoInicialPagina() {
		return this.posicaoInicialPagina;
	}

	public int getPosicaoFinalPagina() {
		return this.posicaoFinalPagina;
	}

	public int getTotalRegistros() {
		return this.totalRegistros;
	}

	public int getTotalPaginas() {
		return this.totalPaginas;
	}

	void setTotalRegistros(int totalRegistros) {
		this.totalRegistros = totalRegistros;

		if (this.totalRegistros > 0) {
			this.totalPaginas = (int) Math.ceil(((double) this.totalRegistros) / this.quantidadeRegistroPagina);

			if (this.numeroPagina > this.totalPaginas) {
				this.limpar();
			}
			else if (this.posicaoFinalPagina > this.totalRegistros) {
				this.posicaoFinalPagina = this.totalRegistros;
			}
		}
		else {
			this.limpar();
		}
	}

	private void limpar() {
		this.numeroPagina = 0;
		this.posicaoInicialPagina = 0;
		this.posicaoFinalPagina = 0;
		this.totalRegistros = 0;
		this.totalPaginas = 0;
	}
}