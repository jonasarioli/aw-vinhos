package com.algaworks.vinhos.controller;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.algaworks.vinhos.model.TipoVinho;
import com.algaworks.vinhos.model.Vinho;
import com.algaworks.vinhos.repository.Vinhos;
import com.algaworks.vinhos.repository.filter.VinhoFilter;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@WebMvcTest(VinhosController.class)
public class VinhosControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	Vinhos vinhosMock;

	@Autowired
	ObjectMapper objectMapper;

	@Test
	public void findAll_ShouldAddVinhoEntriesToModelAndRenderVinhos() throws Exception {
		Vinho first = new Vinho();
		first.setCodigo(1L);
		first.setNome("Lorem ipsum");
		first.setTipo(TipoVinho.TINTO);
		first.setValor(new BigDecimal(5.90));

		VinhoFilter filter = new VinhoFilter();
		filter.setNome("orem");

		when(vinhosMock.findByNomeContainingIgnoreCase("orem")).thenReturn(Arrays.asList(first));

		this.mockMvc
				.perform(get("/vinhos").with(user("admin").password("admin")).accept(MediaType.TEXT_PLAIN)
						.flashAttr("vinhoFilter", filter))
				.andExpect(status().isOk()).andExpect(view().name("vinho/pesquisa-vinhos"))
				.andExpect(model().attribute("vinhos", hasSize(1)))
				.andExpect(model().attribute("vinhos",
						hasItem(allOf(hasProperty("codigo", is(1L)), hasProperty("nome", is("Lorem ipsum")),
								hasProperty("tipo", is(TipoVinho.TINTO)),
								hasProperty("valor", is(new BigDecimal(5.90)))))));
	}
	
	@Test
    public void add_NewTodoEntry_ShouldAddTodoEntryAndRenderViewTodoEntryView() throws Exception {
		Vinho added = new Vinho();
		added.setCodigo(1L);
		added.setNome("Lorem ipsum");
		added.setTipo(TipoVinho.TINTO);
		added.setValor(new BigDecimal(5.90));
 
        when(vinhosMock.save(isA(Vinho.class))).thenReturn(added);
 
        mockMvc.perform(post("/vinhos/novo").with(user("admin").password("admin"))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .flashAttr("vinhoFilter", added)
                .sessionAttr("vinho", new Vinho())
        )
                .andExpect(status().isOk())
                .andExpect(view().name("vinho/cadastro-vinho"));
 
        ArgumentCaptor<Vinho> formObjectArgument = ArgumentCaptor.forClass(Vinho.class);
        verify(vinhosMock, times(1)).save(formObjectArgument.capture());
        verifyNoMoreInteractions(vinhosMock);
 
        Vinho formObject = formObjectArgument.getValue();
 
        assertThat(formObject.getNome(), is("Lorem ipsum"));
        assertNull(formObject.getCodigo());
    }
}
