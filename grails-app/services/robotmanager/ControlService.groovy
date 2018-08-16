package robotmanager

import grails.converters.JSON
import grails.transaction.Transactional
import org.grails.web.json.JSONArray

@Transactional
class ControlService {

    private static Boolean running = false

    def start() {
        new URL("http://localhost/velocity/set?move=0&turn=0").text
        running = true
        1
    }

    def stop() {
        running = false
        new URL("http://localhost/velocity/set?move=0&turn=0").text
        0
    }

    private lastDataSum

    def manage() {
        def data = JSON.parse(new URL('http://localhost/environment/getRanges').text) as JSONArray

        if (running) {
            if (lastDataSum == data.sum())
                new URL("http://localhost/velocity/set?move=0&turn=0").text
            else {
                new URL("http://localhost/velocity/set?move=${findMoveSpeed(data)}&turn=${findTurnSpeed(data)}").text
                lastDataSum = data.sum()
            }
        }
    }

    Boolean turning = false
    Float currentTurnSpeed = 0.0

    Float findTurnSpeed(JSONArray data) {
        def frontDistance = findMin(data[170..190])
        def rightDistance = findMin(data[80..100])
        def leftDistance = findMin(data[260..280])

        if (frontDistance < 0.5 && (frontDistance < rightDistance || frontDistance < leftDistance)) {
            if (rightDistance > leftDistance && !turning) {
                turning = true
                currentTurnSpeed = -0.4
            } else if (!turning) {
                turning = true
                currentTurnSpeed = 0.4
            }

            currentTurnSpeed
        } else {
            if (rightDistance > leftDistance * 1.5)
                -0.1
            else if (leftDistance > rightDistance * 1.5)
                0.1
            else
                0.0
        }
    }

    Float findMoveSpeed(JSONArray data) {
        def frontDistance = findMin(data[170..190])
        def rightDistance = findMin(data[80..100])
        def leftDistance = findMin(data[260..280])
//        println frontDistance
        if (frontDistance < 0.15)
            -0.1
        else if (frontDistance > 0.5 || (frontDistance > rightDistance && frontDistance > leftDistance)) {
            turning = false
            frontDistance / 5
        } else
            0.0
    }

    def findMin(List<Double> data) {
        def refinedData = data.findAll { it }
        def noiseFound = true
        while (noiseFound) {
            noiseFound = false
            for (def i = 0; i < refinedData.size(); i++) {
                if (Math.abs((refinedData[i] - refinedData[(i - 1 + refinedData.size()) % refinedData.size()]) / (refinedData[i] + 1)) > 10
                        || Math.abs((refinedData[i] - refinedData[(i + 1) % refinedData.size()]) / (refinedData[i] + 1)) > 10) {
                    refinedData[i] = refinedData[(i + 1) % refinedData.size()]
                    noiseFound = true
                }
            }
        }
//        (refinedData.sum() ?: 0) / ((refinedData?.size() ?: 0) + 1)
        refinedData.min()
    }
}
