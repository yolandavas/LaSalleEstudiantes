package lasalleestudiantes

import grails.validation.ValidationException
import org.grails.taglib.GrailsTagException

import java.lang.reflect.InvocationTargetException

import static org.springframework.http.HttpStatus.*

class EstudianteController {

    EstudianteService estudianteService

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond estudianteService.list(params), model: [estudianteCount: estudianteService.count()]
    }

    def show(Long id) {
        respond estudianteService.get(id)
    }

    def create() {
        respond new Estudiante(params)
    }

    def save(Estudiante estudiante) {
        if (estudiante == null) {
            notFound()
            return
        }

        try {
            estudianteService.save(estudiante)
        } catch (ValidationException e) {
            respond estudiante.errors, view: 'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'estudiante.label', default: 'Estudiante'), estudiante.id])
                redirect estudiante
            }
            '*' { respond estudiante, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond estudianteService.get(id)
    }

    def update(Estudiante estudiante) {
        if (estudiante == null) {
            notFound()
            return
        }

        try {
            estudianteService.save(estudiante)
        } catch (ValidationException e) {
            respond estudiante.errors, view: 'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'estudiante.label', default: 'Estudiante'), estudiante.id])
                redirect estudiante
            }
            '*' { respond estudiante, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        estudianteService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'estudiante.label', default: 'Estudiante'), id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'estudiante.label', default: 'Estudiante'), params.id])
                redirect action: "index", method: "GET"
            }
            '*' { render status: NOT_FOUND }
        }
    }

    def encontrarEspecialidades(Long nivelAcademicoId) {
        println "id -> ${nivelAcademicoId} "

        try {
            NivelAcademico nivelAcademico = NivelAcademico.get(nivelAcademicoId)
            def especializaciones = []
            if (nivelAcademico != null) {
                especializaciones = Especializacion.findAllByNivelAcademico(nivelAcademico, [order: 'name'])
            }

            // for (item in especializaciones) println "Especializacion ${item.nombre}"

            render g.select(
                    id: 'especializacion',
                    name: 'especializacion',
                    from: especializaciones,
                    optionKey: 'id',
                    noSelection: ['': ' - Elige una especialidad - '])
        } catch (NullPointerException | GrailsTagException | InvocationTargetException ex) {
            println "Parametro nulo"
            render g.select(from: [], optionKey: 'id', noSelection: ['': ' - Elige una especialidad - '])
        }
    }
}
