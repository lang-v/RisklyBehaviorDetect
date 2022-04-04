package com.sl.web.server.event

import com.google.common.eventbus.Subscribe
import com.sun.deploy.util.ArrayUtil
import java.util.*

/**
 * 通过guava eventbus 注册事件监听
 * 仅处理 检测到危险行为时进行事件记录，报警等操作
 *
 * 感觉这个类废了，邮件发送必须查询数据库找到负责人
 * @see com.sl.web.server.email.EmailSender
 */
class EventReceiver {

    /**
     * 其他动作都当作无危险行为
     */
    private val levelTwo = arrayOf(4,6/*跳跃*/)
    private val levelThree = arrayOf(51/*射击*/, 57/*扔东西*/, 63 /*打架*/)

    /**
     * 当检测到行为时
     */
    @Subscribe
    fun onActionNotify(resourceId:Long ,notifyTime: Long, actionCodes: Array<Int>) {
        println("EventReceiver: $actionCodes at ${Date(notifyTime)}")

        val maxLevel = getLevel(actionCodes).maxOf { it }
        // 每次处理最高级别的行为就行
        when (maxLevel) {
            0 -> {
                // 没什么危险
                // 啥也不做
            }
            1 -> {
                // 有点危险
                // 记录日志，前端弹窗提示一下
            }
            2 -> {
                // 相当危险
                // 记录日志，发送警告邮件（报警）

            }
        }
    }

    private fun getLevel(actionCodes: Array<Int>): Array<Int> {
        val levels = Array<Int>(actionCodes.size) { 0 }
        actionCodes.forEachIndexed { index, i ->
            if (levelThree.contains(i))
                levels[index] = 2
            else if (levelTwo.contains(i))
                levels[index] = 1
            else
                levels[index] = 0
        }
        return levels
    }

    /**
     * actionCode 对应的行为
     * {
    "bend/bow (at the waist)": 0,
    "crawl": 1,
    "crouch/kneel": 2,
    "dance": 3,
    "fall down": 4,
    "get up": 5,
    "jump/leap": 6,
    "lie/sleep": 7,
    "martial art": 8,
    "run/jog": 9,
    "sit": 10,
    "stand": 11,
    "swim": 12,
    "walk": 13,
    "answer phone": 14,
    "brush teeth": 15,
    "carry/hold (an object)": 16,
    "catch (an object)": 17,
    "chop": 18,
    "climb (e.g., a mountain)": 19,
    "clink glass": 20,
    "close (e.g., a door, a box)": 21,
    "cook": 22,
    "cut": 23,
    "dig": 24,
    "dress/put on clothing": 25,
    "drink": 26,
    "drive (e.g., a car, a truck)": 27,
    "eat": 28,
    "enter": 29,
    "exit": 30,
    "extract": 31,
    "fishing": 32,
    "hit (an object)": 33,
    "kick (an object)": 34,
    "lift/pick up": 35,
    "listen (e.g., to music)": 36,
    "open (e.g., a window, a car door)": 37,
    "paint": 38,
    "play board game": 39,
    "play musical instrument": 40,
    "play with pets": 41,
    "point to (an object)": 42,
    "press": 43,
    "pull (an object)": 44,
    "push (an object)": 45,
    "put down": 46,
    "read": 47,
    "ride (e.g., a bike, a car, a horse)": 48,
    "row boat": 49,
    "sail boat": 50,
    "shoot": 51,
    "shovel": 52,
    "smoke": 53,
    "stir": 54,
    "take a photo": 55,
    "text on/look at a cellphone": 56,
    "throw": 57,
    "touch (an object)": 58,
    "turn (e.g., a screwdriver)": 59,
    "watch (e.g., TV)": 60,
    "work on a computer": 61,
    "write": 62,
    "fight/hit (a person)": 63,
    "give/serve (an object) to (a person)": 64,
    "grab (a person)": 65,
    "hand clap": 66,
    "hand shake": 67,
    "hand wave": 68,
    "hug (a person)": 69,
    "kick (a person)": 70,
    "kiss (a person)": 71,
    "lift (a person)": 72,
    "listen to (a person)": 73,
    "play with kids": 74,
    "push (another person)": 75,
    "sing to (e.g., self, a person, a group)": 76,
    "take (an object) from (a person)": 77,
    "talk to (e.g., self, a person, a group)": 78,
    "watch (a person)": 79
    }
     */

}