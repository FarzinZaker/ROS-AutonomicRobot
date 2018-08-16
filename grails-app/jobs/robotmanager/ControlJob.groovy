package robotmanager

class ControlJob {

    static concurrent = false

    static triggers = {
        simple repeatInterval: 1000l // execute job once in 5 seconds
    }

    def controlService

    def execute() {

        controlService.manage()
    }
}
