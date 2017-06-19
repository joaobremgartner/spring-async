package br.com.jopss.springasync.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Classe mae de todas os modelos. Auxilia na estrategia de banco de dados,
 * escondendo objetos tecnicos dos modelos, ou acoes simples e repetitivas.
 *
 * A criacao do EntityManager e das Transacoes estao gerenciados manualmente (ao
 * inves de delegar ao container), pois os modelos nao sao gerenciados pelo
 * Spring.
 */
@MappedSuperclass
@JsonAutoDetect(fieldVisibility=Visibility.ANY, getterVisibility = Visibility.NONE, isGetterVisibility = Visibility.NONE, setterVisibility = Visibility.NONE)
public abstract class Modelos implements Serializable {
        
	private static final long serialVersionUID = -1340481266616282366L;
        
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataCriacao;
        
	public abstract String getId();
        
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(getId()).toHashCode();
	}
        
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof Modelos) {
			final Modelos other = (Modelos) obj;
			return new EqualsBuilder().append(getId(), other.getId()).isEquals();
		} else {
			return false;
		}
	}
        
	@Override
	public String toString() {
		return this.getClass().getName()+"[id=" + getId() + "]";
	}
        
	public boolean isSalvo() {
		return getId() != null;
	}

	public boolean isNaoSalvo() {
		return !isSalvo();
	}

        public Date getDataCriacao() {
                return dataCriacao;
        }

        public void setDataCriacao(Date dataCriacao) {
                this.dataCriacao = dataCriacao;
        }
        
}
