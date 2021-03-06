package lasalleestudiantes

class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?(.$format)?"{
            constraints {
                // apply constraints here
            }
        }

        "/"(controller:"home")
        "500"(view:'/error')
        "404"(view:'/notFound')

        "/register"(controller:"home", action:"register")
        "/alert"(controller:"home", action:"alert")
    }
}
