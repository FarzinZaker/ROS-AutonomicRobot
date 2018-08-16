package robotmanager

import grails.converters.JSON

class MappingController {

    def controlService

    def show() {

    }

    def start() {
        render controlService.start()
    }

    def stop() {
        render controlService.stop()
    }

    def mapJSON() {
        try {
            def data = JSON.parse(new URL('http://localhost/environment/getMap').text)
            render data as JSON
        }
        catch (ignored) {
            render([] as JSON)
        }
    }
}
