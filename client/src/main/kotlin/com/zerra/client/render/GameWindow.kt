package com.zerra.client.render

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11C.glViewport
import org.lwjgl.system.MemoryStack
import org.lwjgl.system.MemoryUtil.NULL
import org.lwjgl.system.NativeResource
import java.nio.IntBuffer

/**
 *
 * @author Boston Vanseghi
 * @since 0.0.1
 */
object GameWindow : NativeResource {

    private var windowId = NULL
    var closeRequested = false
        private set
    var windowWidth = 0
        private set
    var windowHeight = 0
        private set
    var framebufferWidth = 0
        private set
    var framebufferHeight = 0
        private set

    fun init() {
        GLFWErrorCallback.createPrint(System.err).set()
        if (!glfwInit())
            throw RuntimeException("Failed to initialize GLFW")
    }

    fun create(width: Int, height: Int, name: CharSequence) {
        if (windowId != NULL)
            throw IllegalStateException("Multiple windows are not supported")

        // Create window
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 1)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1)
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)

        windowId = glfwCreateWindow(width, height, name, NULL, NULL)
        if (windowId == NULL)
            throw IllegalStateException("Failed to create GLFW Window")

        // Position Window
        MemoryStack.stackPush().use {
            val pWidth: IntBuffer = it.mallocInt(1)
            val pHeight: IntBuffer = it.mallocInt(1)

            glfwGetWindowSize(windowId, pWidth, pHeight)
            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!

            glfwSetWindowPos(
                windowId,
                (vidmode.width() - pWidth[0]) / 2,
                (vidmode.height() - pHeight[0]) / 2
            )
        }

        glfwMakeContextCurrent(windowId)
        glfwShowWindow(windowId)

        // Add listeners
        glfwSetWindowCloseCallback(windowId) {
            closeRequested = true
        }
        glfwSetWindowSizeCallback(windowId) { _, w, h ->
            windowWidth = w
            windowHeight = h
        }
        glfwSetFramebufferSizeCallback(windowId) { _, w, h ->
            framebufferWidth = w
            framebufferHeight = h
            glViewport(0, 0, w, h)
        }

        GL.createCapabilities()
    }

    fun pollEvents() {
        glfwSwapBuffers(windowId)
        glfwPollEvents()
    }

    fun destroy() {
        if (windowId == NULL)
            return

        glfwFreeCallbacks(windowId)
        glfwDestroyWindow(windowId)
        windowId = NULL
    }

    override fun free() {
        destroy()
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }
}