VERSION=$(shell cat VERSION)
WAR=WeblichtStanfordServices\#$(VERSION).war
TGZ=WeblichtStanfordServices-$(VERSION).tgz

include ../master.mk

