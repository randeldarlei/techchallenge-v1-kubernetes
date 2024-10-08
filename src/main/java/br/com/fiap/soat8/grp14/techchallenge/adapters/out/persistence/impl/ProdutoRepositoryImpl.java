package br.com.fiap.soat8.grp14.techchallenge.adapters.out.persistence.impl;

import br.com.fiap.soat8.grp14.techchallenge.adapters.out.persistence.ProdutoSpringRepository;
import br.com.fiap.soat8.grp14.techchallenge.application.ports.out.ProdutoRepositoryPort;
import br.com.fiap.soat8.grp14.techchallenge.domain.enums.CategoriaProduto;
import br.com.fiap.soat8.grp14.techchallenge.domain.exceptions.DataIntegrityException;
import br.com.fiap.soat8.grp14.techchallenge.domain.exceptions.EntityNotFoundException;
import br.com.fiap.soat8.grp14.techchallenge.domain.models.Produto;
import br.com.fiap.soat8.grp14.techchallenge.adapters.out.persistence.entities.ProdutoEntity;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProdutoRepositoryImpl implements ProdutoRepositoryPort {

    private final ProdutoSpringRepository produtoSpringRepository;

    public ProdutoRepositoryImpl(ProdutoSpringRepository produtoSpringRepository) {
        this.produtoSpringRepository = produtoSpringRepository;
    }

    @Override
    public List<Produto> buscarTodos() {
        List<ProdutoEntity> produtoEntities = this.produtoSpringRepository.findAll();
        return produtoEntities.stream().map(ProdutoEntity::toProduto).collect(Collectors.toList());
    }

    @Override
    public List<Produto> buscarPorCategoria(CategoriaProduto categoriaProduto) {
        List<ProdutoEntity> produtoEntitiesCategoria = this.produtoSpringRepository.findByCategoriaProduto(categoriaProduto);
        return produtoEntitiesCategoria.stream().map(ProdutoEntity::toProduto).collect(Collectors.toList());
    }

    @Override
    public Produto salvarProduto(Produto produto) {
        ProdutoEntity produtoEntity = new ProdutoEntity(produto);
        this.produtoSpringRepository.save(produtoEntity);
        return produtoEntity.toProduto();
    }

    @Override
    public Produto buscarPorId(Long id) {
        Optional<ProdutoEntity> produtoEntity = Optional.ofNullable(this.produtoSpringRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado")));
        return produtoEntity.map(ProdutoEntity::toProduto).orElse(null);
    }

    @Override
    public void deletarProduto(Long id) {
        try {
            ProdutoEntity produtoEntity = produtoSpringRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
            this.produtoSpringRepository.delete(produtoEntity);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possivel deletar o produto, " +
                    "pois o mesmo está associado a um pedido");
        }
    }

    @Override
    public Produto atualizarProduto(Long id, Produto produto) {

        ProdutoEntity produtoExistente = produtoSpringRepository.
                findById(id).orElseThrow(() ->
                        new EntityNotFoundException("Produto não encontrado"));
        produtoExistente.setNome(produto.getNome());
        produtoExistente.setDescricao(produto.getDescricao());
        produtoExistente.setValor(produto.getValor());
        produtoExistente.setCategoriaProduto(produto.getCategoriaProduto());

        this.produtoSpringRepository.save(produtoExistente);
        return produtoExistente.toProduto();
    }
}



