package ensah.absencemanagement.services.types_seances;

import ensah.absencemanagement.dtos.types_seances.TypeSeanceDTO;
import ensah.absencemanagement.dtos.types_seances.TypeSeanceMapper;
import ensah.absencemanagement.dtos.types_seances.TypeSeanceRequest;
import ensah.absencemanagement.exceptions.not_found.TypeSeanceNotFoundException;
import ensah.absencemanagement.models.types_seance.TypeSeance;
import ensah.absencemanagement.repositories.TypeSeanceRepostiory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TypeSeanceService {

    private final TypeSeanceRepostiory typeSeanceRepostiory;
    private final TypeSeanceMapper typeSeanceMapper;

    @Autowired
    public TypeSeanceService(TypeSeanceRepostiory typeSeanceRepostiory, TypeSeanceMapper typeSeanceMapper) {
        this.typeSeanceRepostiory = typeSeanceRepostiory;
        this.typeSeanceMapper = typeSeanceMapper;
    }

    public List<TypeSeanceDTO> getAllTypesSeances() {
        return typeSeanceMapper.map(typeSeanceRepostiory.findAll());
    }

    public TypeSeanceDTO getTypeSeanceById(Long typeSeanceId) {
        return typeSeanceMapper.map(findTypeSeanceById(typeSeanceId));
    }

    public Long createTypeSeance(TypeSeanceRequest request) {
        TypeSeance savedTypeSeance = typeSeanceRepostiory.save(
                typeSeanceMapper.createTypeSeance(request)
        );
        return savedTypeSeance.getId();
    }

    public void updateTypeSeanceById(Long typeSeanceId, TypeSeanceRequest request) {
        TypeSeance typeSeance = findTypeSeanceById(typeSeanceId);
        typeSeanceMapper.updateTypeSeance(request, typeSeance);

        typeSeanceRepostiory.save(typeSeance);
    }

    public void deleteTypeSeanceById(Long typeSeanceId) {
        TypeSeance typeSeance = findTypeSeanceById(typeSeanceId);
        typeSeanceRepostiory.delete(typeSeance);
    }

    private TypeSeance findTypeSeanceById(Long id) {
        return typeSeanceRepostiory.findById(id)
                .orElseThrow(() -> new TypeSeanceNotFoundException(id));
    }

}
