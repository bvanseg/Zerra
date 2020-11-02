package com.zerra.client.util

import com.zerra.client.render.GameWindow
import org.lwjgl.opengl.GL33C.*

object HardwareConstraints {
    val maxTextureSize: Int by lazy { glGetInteger(GL_MAX_TEXTURE_SIZE) }
    val maxVaryingFloats: Int by lazy { glGetInteger(GL_MAX_VARYING_FLOATS) }

    val maxUniformBufferBindings: Int by lazy { glGetInteger(GL_MAX_UNIFORM_BUFFER_BINDINGS) }
    val maxCombinedUniformBlocks: Int by lazy { glGetInteger(GL_MAX_COMBINED_UNIFORM_BLOCKS) }
    val maxCombinedTextureImageUnits: Int by lazy { glGetInteger(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS) }
    val maxTransformFeedbackSeparateAttributes: Int by lazy { glGetInteger(GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_ATTRIBS) }
    val maxTransformFeedbackSeparateComponents: Int by lazy { glGetInteger(GL_MAX_TRANSFORM_FEEDBACK_SEPARATE_COMPONENTS) }
    val maxTransformFeedbackInterleavedComponents: Int by lazy { glGetInteger(GL_MAX_TRANSFORM_FEEDBACK_INTERLEAVED_COMPONENTS) }

    val maxVertexUniformComponents: Int by lazy { glGetInteger(GL_MAX_VERTEX_UNIFORM_COMPONENTS) }
    val maxVertexUniformBlocks: Int by lazy { glGetInteger(GL_MAX_VERTEX_UNIFORM_BLOCKS) }
    val maxVertexAttributes: Int by lazy { glGetInteger(GL_MAX_VERTEX_ATTRIBS) }
    val maxVertexOutputComponents: Int by lazy { glGetInteger(GL_MAX_VERTEX_OUTPUT_COMPONENTS) }
    val maxVertexTextureImageUnits: Int by lazy { glGetInteger(GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS) }

    val maxGeometryUniformComponents: Int by lazy { glGetInteger(GL_MAX_GEOMETRY_UNIFORM_COMPONENTS) }
    val maxGeometryUniformBlocks: Int by lazy { glGetInteger(GL_MAX_GEOMETRY_UNIFORM_BLOCKS) }
    val maxGeometryInputComponents: Int by lazy { glGetInteger(GL_MAX_GEOMETRY_INPUT_COMPONENTS) }
    val maxGeometryOutputComponents: Int by lazy { glGetInteger(GL_MAX_GEOMETRY_OUTPUT_COMPONENTS) }
    val maxGeometryTextureImageUnits: Int by lazy { glGetInteger(GL_MAX_GEOMETRY_TEXTURE_IMAGE_UNITS) }

    val maxFragmentUniformComponents: Int by lazy { glGetInteger(GL_MAX_FRAGMENT_UNIFORM_COMPONENTS) }
    val maxFragmentUniformBlocks: Int by lazy { glGetInteger(GL_MAX_FRAGMENT_UNIFORM_BLOCKS) }
    val maxFragmentInputComponents: Int by lazy { glGetInteger(GL_MAX_FRAGMENT_INPUT_COMPONENTS) }
    val maxFragmentDrawBuffers: Int by lazy { glGetInteger(GL_MAX_DRAW_BUFFERS) }
    val maxFragmentTextureImageUnits: Int by lazy { glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS) }

    @JvmStatic
    fun main(args: Array<String>) {
        GameWindow.init()
        GameWindow.create(1280, 720, "Hardware Constants", true)

        println("Max Texture Size: $maxTextureSize")
        println("Max Varying Floats: $maxVaryingFloats")

        println("Max Uniform Buffer Bindings: $maxUniformBufferBindings")
        println("Max Combined Uniform Blocks: $maxCombinedUniformBlocks")
        println("Max Combined Texture Image Units: $maxCombinedTextureImageUnits")
        println("Max Transform Feedback Separate Attributes: $maxTransformFeedbackSeparateAttributes")
        println("Max Transform Feedback Separate Components: $maxTransformFeedbackSeparateComponents")
        println("Max Transform Feedback Interleaved Components: $maxTransformFeedbackInterleavedComponents")

        println("Max Vertex Uniform Components: $maxVertexUniformComponents")
        println("Max Vertex Uniform Blocks: $maxVertexUniformBlocks")
        println("Max Vertex Attributes: $maxVertexAttributes")
        println("Max Vertex Output Components: $maxVertexOutputComponents")
        println("Max Vertex Texture Image Units: $maxVertexTextureImageUnits")

        println("Max Geometry Uniform Components: $maxGeometryUniformComponents")
        println("Max Geometry Uniform Blocks: $maxGeometryUniformBlocks")
        println("Max Geometry Input Components: $maxGeometryInputComponents")
        println("Max Geometry Output Components: $maxGeometryOutputComponents")
        println("Max Geometry Texture Image Units: $maxGeometryTextureImageUnits")

        println("Max Fragment Uniform Components: $maxFragmentUniformComponents")
        println("Max Fragment Uniform Blocks: $maxFragmentUniformBlocks")
        println("Max Fragment Input Components: $maxFragmentInputComponents")
        println("Max Fragment Draw Buffers: $maxFragmentDrawBuffers")
        println("Max Fragment Texture Image Units: $maxFragmentTextureImageUnits")

        while (!GameWindow.closeRequested)
            GameWindow.update()
        GameWindow.free()
    }
}