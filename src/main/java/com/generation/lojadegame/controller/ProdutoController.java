package com.generation.lojadegame.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import com.generation.lojadegame.model.Produto;
import com.generation.lojadegame.repository.CategoriaRepository;
import com.generation.lojadegame.repository.ProdutoRepository;

import jakarta.validation.Valid;



	@RestController
	@RequestMapping("/produtos")
	@CrossOrigin(allowedHeaders = "*", origins = "*")
	public class ProdutoController {
		
		@Autowired 
		private ProdutoRepository produtoRepository;
		
		@Autowired
	    private CategoriaRepository categoriaRepository;
		
		@GetMapping
		public ResponseEntity<List<Produto>>getAll(){
			return ResponseEntity.ok(produtoRepository.findAll());
		}
			@GetMapping("/{id}")
			public ResponseEntity<Produto> getById(@PathVariable Long id){
				return produtoRepository.findById(id)
						.map(resp -> ResponseEntity.ok(resp))
						.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
			}
			
			@GetMapping("/nome/{nome}")
			public ResponseEntity<List<Produto>>getByNome(@PathVariable String nome){
				return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
			
			}
			
			@PostMapping
			public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto){
			    if (produto.getId() != null && produtoRepository.existsById(produto.getId()))
			        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Produto Já Existe!", null);

			    return ResponseEntity.status(HttpStatus.CREATED)
			            .body(produtoRepository.save(produto));
			    
			}
			@PutMapping
			public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
			    if (produtoRepository.existsById(produto.getId())) { // Verifica se o produto existe
			        
			      
					if (categoriaRepository.existsById(produto.getCategoria().getId())) { // Verifica se a categoria existe
			            return ResponseEntity.status(HttpStatus.OK)
			                    .body(produtoRepository.save(produto)); // Atualiza o produto
			        }
			        
			        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria não existe!", null);
			    }

			    return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Retorna 404 se o produto não existir
			}

			
			@ResponseStatus(HttpStatus.NO_CONTENT)
			@DeleteMapping("/{id}")
			public void delete(@PathVariable Long id) {
				Optional<Produto> produto = produtoRepository.findById(id);
				
				if(produto.isEmpty())
					throw new ResponseStatusException(HttpStatus.NOT_FOUND);
				
				produtoRepository.deleteById(id);				
				
			}
			
		}
		
		
	
	
	
				
				
			

			
	
		
		