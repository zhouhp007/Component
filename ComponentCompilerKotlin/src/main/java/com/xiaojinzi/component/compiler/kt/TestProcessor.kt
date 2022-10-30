package com.xiaojinzi.component.compiler.kt

import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated

class TestProcessor : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        TODO()
    }

}