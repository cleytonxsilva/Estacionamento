package br.com.uniamerica.estacionamento.controller;


import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import br.com.uniamerica.estacionamento.service.MovimentacaoService;
import br.com.uniamerica.estacionamento.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin("*")
@RequestMapping(value = "/api/veiculo")
public class VeiculoController {
    @Autowired
    private VeiculoService veiculoService;
    @Autowired
    private MovimentacaoService movimentacaoService;
    @Autowired
    private VeiculoRepository veiculoRepository;

    @GetMapping
    public ResponseEntity<?> findByIdRequest(@RequestParam("id") final Long id){
        final Veiculo veiculo = this.veiculoService.findById(id).orElse(null);
        return veiculo == null
                ? ResponseEntity.badRequest().body("Nenhum valor encontrado")
                : ResponseEntity.ok(veiculo);
    }
    @GetMapping("/placa:{placa}")
    public ResponseEntity<?> findByPlaca(@PathVariable ("placa") final String placaCarro){
        final Veiculo veiculoByPlaca = veiculoRepository.findByPlacaCarro(placaCarro);

        return ResponseEntity.ok(veiculoByPlaca);
    }

    @GetMapping("/lista")
    public ResponseEntity<?> listaCompleta(){
        return ResponseEntity.ok(this.veiculoService.findAll());
    }

    @GetMapping("/ativos")
    public ResponseEntity<?> findByAtivo(){
        final List<Veiculo> veiculos = this.veiculoService.findByAtivoTrue();
        return ResponseEntity.ok(veiculos);
    }
    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody final Veiculo veiculo) {
        try {
            this.veiculoService.cadastrar(veiculo);
            return ResponseEntity.ok("Registro cadastrado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Veiculo veiculo){
        try{
            this.veiculoService.editar(id, veiculo);
            return ResponseEntity.ok("Registro editado com sucesso");
        }
        catch (RuntimeException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping
    public ResponseEntity<?> excluir(@RequestParam("id") final Long id){
        try {
            final Veiculo veiculo = this.veiculoService.findById(id).orElse(null);
            if(veiculo == null){
                throw new Exception("Registro inexistente");
            }
                this.veiculoService.excluir(id);
                return ResponseEntity.ok("Registro deletado com sucesso");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
