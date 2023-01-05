package kr.esei.library.constant

import android.view.LayoutInflater
import android.view.ViewGroup

public typealias ActivityInflater<T> = (LayoutInflater) -> T
public typealias FragmentInflater<T> = (LayoutInflater, ViewGroup?, Boolean) -> T