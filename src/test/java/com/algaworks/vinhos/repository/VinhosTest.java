package com.algaworks.vinhos.repository;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.algaworks.vinhos.model.TipoVinho;
import com.algaworks.vinhos.model.Vinho;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VinhosTest {

	@Autowired
    private TestEntityManager entityManager;

    @Autowired
    private Vinhos vinhosRepository;

    @Test
    public void findByNomeContainingIgnoreCaseTest() {
    	
    	Vinho vinho = new Vinho();
    	vinho.setNome("Teste");
    	vinho.setTipo(TipoVinho.TINTO);
    	vinho.setValor(new BigDecimal(4.90));
        entityManager.persist(vinho);
        
        List<Vinho> vinhos = vinhosRepository.findByNomeContainingIgnoreCase("es");
        assertEquals(1, vinhos.size());
        assertEquals("Teste", vinhos.get(0).getNome());        
    }
    
    @Test
    public void notFindByNomeContainingIgnoreCaseTest() {
    	
    	Vinho vinho = new Vinho();
    	vinho.setNome("Teste");
    	vinho.setTipo(TipoVinho.TINTO);
    	vinho.setValor(new BigDecimal(4.90));
        entityManager.persist(vinho);
        
        List<Vinho> vinhos = vinhosRepository.findByNomeContainingIgnoreCase("out");
        assertEquals(0, vinhos.size());        
    }

}
