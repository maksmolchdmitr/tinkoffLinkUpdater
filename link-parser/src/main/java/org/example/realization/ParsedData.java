package org.example.realization;

public abstract sealed class ParsedData permits
        GithubLinkParser.GithubData,
        StackoverflowLinkParser.StackoverflowData,
        UrlParser.EmptyData
{}