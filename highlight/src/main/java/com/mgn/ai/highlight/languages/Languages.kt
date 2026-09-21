package com.mgn.ai.highlight.languages

import com.mgn.ai.highlight.core.Language
import com.mgn.ai.highlight.languages.bash.bash
import com.mgn.ai.highlight.languages.c.c
import com.mgn.ai.highlight.languages.cmake.cmake
import com.mgn.ai.highlight.languages.cpp.cpp
import com.mgn.ai.highlight.languages.csharp.csharp
import com.mgn.ai.highlight.languages.css.css
import com.mgn.ai.highlight.languages.dart.dart
import com.mgn.ai.highlight.languages.diff.diff
import com.mgn.ai.highlight.languages.dockerfile.dockerfile
import com.mgn.ai.highlight.languages.go.go
import com.mgn.ai.highlight.languages.glsl.glsl
import com.mgn.ai.highlight.languages.ini.ini
import com.mgn.ai.highlight.languages.java.java
import com.mgn.ai.highlight.languages.javascript.javascript
import com.mgn.ai.highlight.languages.json.json
import com.mgn.ai.highlight.languages.kotlin.kotlin
import com.mgn.ai.highlight.languages.latex.latex
import com.mgn.ai.highlight.languages.lua.lua
import com.mgn.ai.highlight.languages.markdown.markdown
import com.mgn.ai.highlight.languages.php.php
import com.mgn.ai.highlight.languages.powershell.powershell
import com.mgn.ai.highlight.languages.properties.properties
import com.mgn.ai.highlight.languages.python.python
import com.mgn.ai.highlight.languages.rust.rust
import com.mgn.ai.highlight.languages.ruby.ruby
import com.mgn.ai.highlight.languages.sql.sql
import com.mgn.ai.highlight.languages.swift.swift
import com.mgn.ai.highlight.languages.typescript.typescript
import com.mgn.ai.highlight.languages.xml.xml
import com.mgn.ai.highlight.languages.yaml.yaml

/**
 * Every grammar bundled with the highlighter.
 *
 * Each entry builds a fresh mode tree: compilation mutates modes in place, mirroring `highlight.js`.
 */
internal fun builtinLanguages(): List<Language> = listOf(
    json(),
    ini(),
    cmake(),
    go(),
    glsl(),
    yaml(),
    bash(),
    dockerfile(),
    javascript(),
    typescript(),
    xml(),
    css(),
    dart(),
    java(),
    kotlin(),
    latex(),
    lua(),
    powershell(),
    properties(),
    python(),
    c(),
    cpp(),
    csharp(),
    sql(),
    diff(),
    markdown(),
    rust(),
    ruby(),
    php(),
    swift(),
)
