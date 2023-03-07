package com.owlylabs.platform.util.exceptions

class NoMetaFoundException(metaKey: String) :
    Exception("No data in Manifest for key $metaKey")
